from flask import Flask 
from flask_cors import CORS
from app.routes import bot_blueprint

app = Flask(__name__)
CORS(app)

# Register Blueprints
app.register_blueprint(bot_blueprint)

if __name__ == "__main__":
    app.run(debug=True, port=5000)
