"""
AwareHealth Flask Chatbot API
Disease Information Chatbot with State Management

This Flask API handles the disease chatbot flow using the chat_state table
to persist conversation state across requests.

Deploy to: C:\xampp\htdocs\AwareHealth\aimodel\aware_health\chatbot_api.py
Or integrate into existing flask_api.py
"""

from flask import Flask, request, jsonify
from flask_cors import CORS
import mysql.connector
from mysql.connector import Error
from datetime import datetime, timedelta
import hashlib
import os

app = Flask(__name__)
CORS(app)  # Enable CORS for mobile app

# Database configuration
DB_CONFIG = {
    'host': 'localhost',
    'database': 'awarehealth',
    'user': 'root',
    'password': ''
}

# Yes words - these should NEVER be treated as disease names
YES_WORDS = ["yes", "provide", "ok", "okay", "sure", "fine"]


def get_db_connection():
    """Create and return database connection"""
    try:
        conn = mysql.connector.connect(**DB_CONFIG)
        return conn
    except Error as e:
        print(f"Database connection error: {e}")
        return None


def ensure_chat_state_table():
    """Create chat_state table if it doesn't exist"""
    conn = get_db_connection()
    if not conn:
        return False
    
    try:
        cursor = conn.cursor()
        
        create_table_sql = """
        CREATE TABLE IF NOT EXISTS `chat_state` (
            `chat_id` VARCHAR(255) PRIMARY KEY COMMENT 'Unique chat session identifier',
            `step` VARCHAR(50) NOT NULL DEFAULT 'ASK_DISEASE' COMMENT 'Current step in conversation flow',
            `disease_name` VARCHAR(255) DEFAULT NULL COMMENT 'Name of the selected disease',
            `prevention` TEXT DEFAULT NULL COMMENT 'Prevention tips for the disease',
            `food` TEXT DEFAULT NULL COMMENT 'Food recommendations for the disease',
            `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
            INDEX `idx_step` (`step`),
            INDEX `idx_updated_at` (`updated_at`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Stores chatbot conversation state to prevent reset'
        """
        
        cursor.execute(create_table_sql)
        conn.commit()
        
        # Clean up old sessions (older than 1 hour)
        cleanup_sql = "DELETE FROM chat_state WHERE updated_at < DATE_SUB(NOW(), INTERVAL 1 HOUR)"
        cursor.execute(cleanup_sql)
        conn.commit()
        
        cursor.close()
        conn.close()
        return True
    except Error as e:
        print(f"Error creating chat_state table: {e}")
        if conn:
            conn.close()
        return False


def get_chat_id(request_data):
    """Generate stable chat ID from conversationId or request"""
    conversation_id = request_data.get('conversationId') or request_data.get('conversation_id')
    
    if conversation_id:
        return conversation_id
    
    # Generate stable ID from IP + User-Agent
    ip = request.remote_addr or 'unknown'
    user_agent = request.headers.get('User-Agent', 'unknown')
    return hashlib.md5(f"{ip}{user_agent}awarehealth_chatbot".encode()).hexdigest()


def get_current_state(chat_id):
    """Get current conversation state from database"""
    conn = get_db_connection()
    if not conn:
        return None
    
    try:
        cursor = conn.cursor(dictionary=True)
        cursor.execute(
            "SELECT step, disease_name, prevention, food FROM chat_state WHERE chat_id = %s",
            (chat_id,)
        )
        state = cursor.fetchone()
        cursor.close()
        conn.close()
        return state
    except Error as e:
        print(f"Error getting state: {e}")
        if conn:
            conn.close()
        return None


def save_state(chat_id, step, disease_name=None, prevention=None, food=None):
    """Save conversation state to database"""
    conn = get_db_connection()
    if not conn:
        return False
    
    try:
        cursor = conn.cursor()
        
        if disease_name and prevention and food:
            # Insert or update with all data
            sql = """
            INSERT INTO chat_state (chat_id, step, disease_name, prevention, food)
            VALUES (%s, %s, %s, %s, %s)
            ON DUPLICATE KEY UPDATE
                step = VALUES(step),
                disease_name = VALUES(disease_name),
                prevention = VALUES(prevention),
                food = VALUES(food)
            """
            cursor.execute(sql, (chat_id, step, disease_name, prevention, food))
        else:
            # Update only step
            sql = "UPDATE chat_state SET step = %s WHERE chat_id = %s"
            cursor.execute(sql, (step, chat_id))
        
        conn.commit()
        cursor.close()
        conn.close()
        return True
    except Error as e:
        print(f"Error saving state: {e}")
        if conn:
            conn.close()
        return False


def delete_state(chat_id):
    """Delete conversation state from database"""
    conn = get_db_connection()
    if not conn:
        return False
    
    try:
        cursor = conn.cursor()
        cursor.execute("DELETE FROM chat_state WHERE chat_id = %s", (chat_id,))
        conn.commit()
        cursor.close()
        conn.close()
        return True
    except Error as e:
        print(f"Error deleting state: {e}")
        if conn:
            conn.close()
        return False


def search_disease(disease_name):
    """Search for disease in database"""
    conn = get_db_connection()
    if not conn:
        return None
    
    try:
        cursor = conn.cursor(dictionary=True)
        cursor.execute(
            "SELECT name, symptoms, prevention, food FROM diseases WHERE LOWER(name) LIKE %s LIMIT 1",
            (f"%{disease_name.lower()}%",)
        )
        disease = cursor.fetchone()
        cursor.close()
        conn.close()
        return disease
    except Error as e:
        print(f"Error searching disease: {e}")
        if conn:
            conn.close()
        return None


@app.route('/chatbot', methods=['POST', 'OPTIONS'])
def chatbot():
    """Main chatbot endpoint"""
    
    # Handle CORS preflight
    if request.method == 'OPTIONS':
        return jsonify({'status': 'ok'}), 200
    
    # Ensure chat_state table exists
    ensure_chat_state_table()
    
    # Get request data
    data = request.get_json() or {}
    message = (data.get('message') or '').lower().strip()
    
    # Get chat ID
    chat_id = get_chat_id(data)
    
    # Get current state
    current_state = get_current_state(chat_id)
    current_step = current_state['step'] if current_state else 'ASK_DISEASE'
    disease_name = current_state.get('disease_name') if current_state else None
    prevention = current_state.get('prevention') if current_state else None
    food = current_state.get('food') if current_state else None
    
    # Handle empty message
    if not message:
        if current_step == 'ASK_DISEASE':
            return jsonify({
                'success': True,
                'response': 'Please enter the disease name.',
                'conversationId': chat_id
            })
        elif current_step == 'ASK_PREVENTION':
            return jsonify({
                'success': True,
                'response': 'Please reply yes, provide, okay, sure, or fine to continue.',
                'conversationId': chat_id
            })
        elif current_step == 'ASK_DAYS':
            return jsonify({
                'success': True,
                'response': 'Please enter number of days.',
                'conversationId': chat_id
            })
    
    # Step 1: ASK_DISEASE
    if current_step == 'ASK_DISEASE':
        # Check if message is a yes word (state recovery)
        if message in YES_WORDS:
            if disease_name and prevention and food:
                # Recover state - show prevention + food together
                save_state(chat_id, 'ASK_DAYS')
                return jsonify({
                    'success': True,
                    'response': (
                        f"Here are the prevention tips:\n{prevention}\n\n"
                        f"Food Recommendations:\n{food}\n\n"
                        f"From how many days are you suffering from {disease_name}?"
                    ),
                    'conversationId': chat_id
                })
            else:
                return jsonify({
                    'success': True,
                    'response': 'Please enter the disease name.',
                    'conversationId': chat_id
                })
        
        # Search for disease
        disease = search_disease(message)
        if not disease:
            return jsonify({
                'success': True,
                'response': 'Disease not found. Please try another disease name.',
                'conversationId': chat_id
            })
        
        # Save state and show symptoms
        save_state(
            chat_id,
            'ASK_PREVENTION',
            disease['name'],
            disease['prevention'],
            disease['food']
        )
        
        return jsonify({
            'success': True,
            'response': (
                f"Symptoms of {disease['name']}:\n{disease['symptoms']}\n\n"
                "Can I provide prevention tips?"
            ),
            'conversationId': chat_id
        })
    
    # Step 2: ASK_PREVENTION
    elif current_step == 'ASK_PREVENTION':
        # Check if message is a yes word
        if message not in YES_WORDS:
            return jsonify({
                'success': True,
                'response': 'Please reply yes, provide, okay, sure, or fine to continue.',
                'conversationId': chat_id
            })
        
        # Ensure we have disease data
        if not disease_name or not prevention or not food:
            # Try to reload from database
            current_state = get_current_state(chat_id)
            if current_state:
                disease_name = current_state.get('disease_name') or disease_name
                prevention = current_state.get('prevention') or prevention
                food = current_state.get('food') or food
        
        if not disease_name or not prevention or not food:
            delete_state(chat_id)
            return jsonify({
                'success': True,
                'response': 'Session expired. Please enter the disease name again.',
                'conversationId': chat_id
            })
        
        # Move to ASK_DAYS and show prevention + food together in ONE message
        save_state(chat_id, 'ASK_DAYS')
        
        return jsonify({
            'success': True,
            'response': (
                f"Here are the prevention tips:\n{prevention}\n\n"
                f"Food Recommendations:\n{food}\n\n"
                f"From how many days are you suffering from {disease_name}?"
            ),
            'conversationId': chat_id
        })
    
    # Step 3: ASK_DAYS
    elif current_step == 'ASK_DAYS':
        # Check if message is numeric
        try:
            days = int(message)
        except ValueError:
            return jsonify({
                'success': True,
                'response': 'Please enter number of days.',
                'conversationId': chat_id
            })
        
        # Get disease name for response
        response_disease_name = disease_name or "this disease"
        if not response_disease_name and current_state:
            response_disease_name = current_state.get('disease_name') or response_disease_name
        
        # Clean up session
        delete_state(chat_id)
        
        # Provide advice based on days
        if days > 3:
            return jsonify({
                'success': True,
                'response': (
                    "You have been suffering for more than 3 days.\n"
                    "Please consult Saveetha Hospital.\n"
                    "Appointment Booking Number:\n"
                    "+91 44 2681 0000"
                ),
                'conversationId': ''
            })
        else:
            return jsonify({
                'success': True,
                'response': 'Avoid being alone and take care of your health.',
                'conversationId': ''
            })
    
    # Fallback
    return jsonify({
        'success': False,
        'response': 'An error occurred. Please try again.',
        'conversationId': chat_id
    })


@app.route('/health', methods=['GET'])
def health():
    """Health check endpoint"""
    return jsonify({
        'status': 'success',
        'message': 'Chatbot API is running',
        'service': 'disease_chatbot'
    })


if __name__ == '__main__':
    # Ensure table exists on startup
    ensure_chat_state_table()
    
    print("=" * 50)
    print("AwareHealth Disease Chatbot API")
    print("=" * 50)
    print("Endpoint: /chatbot")
    print("Health check: /health")
    print("=" * 50)
    
    app.run(host='0.0.0.0', port=5000, debug=True)

