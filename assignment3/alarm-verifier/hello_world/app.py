import json
import boto3
import joblib
import pandas as pd
import tempfile
import os

# S3 config
BUCKET_NAME = os.environ.get("MODEL_BUCKET", "your-bucket-name")
MODEL_KEY = "isolation_forest.pkl"

def load_model():
    s3 = boto3.client("s3")
    with tempfile.NamedTemporaryFile(delete=False, suffix=".pkl") as tmp:
        s3.download_file(BUCKET_NAME, MODEL_KEY, tmp.name)
        model = joblib.load(tmp.name)
    return model


def lambda_handler(event, context):
    try:
        body = json.loads(event["body"])

        features = pd.DataFrame([{
            "alarm_type":   body["alarm_type"],
            "severity":     body["severity"],
            "duration_sec": body["duration_sec"],
            "time_of_day":  body["time_of_day"],
            "frequency":    body["frequency"]
        }])

        model = load_model()
        prediction = model.predict(features)[0]
        label = "REAL alarm" if prediction == 1 else "FALSE alarm"

        return {
            "statusCode": 200,
            "body": json.dumps({"result": label, "score": int(prediction)})
        }
    except Exception as e:
        return {
            "statusCode": 500,
            "body": json.dumps({"error": str(e)})
        }