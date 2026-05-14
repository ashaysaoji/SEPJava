import pandas as pd
import numpy as np
from sklearn.ensemble import IsolationForest
import joblib
import os

# ── 1. Generate synthetic alarm data ──────────────────────────────────────────
np.random.seed(42)
n_normal = 900
n_anomaly = 100
n_total = n_normal + n_anomaly

# Normal alarms
normal_data = {
    "alarm_type":    np.random.choice([1, 2, 3], size=n_normal),        # 3 alarm types
    "severity":      np.random.randint(1, 5, size=n_normal),             # severity 1-4
    "duration_sec":  np.random.normal(loc=30, scale=10, size=n_normal).clip(5, 60).astype(int),
    "time_of_day":   np.random.randint(6, 22, size=n_normal),            # 6am - 10pm
    "frequency":     np.random.randint(1, 10, size=n_normal),            # times per day
}

# Anomalous alarms (weird values — high severity, odd hours, extreme duration)
anomaly_data = {
    "alarm_type":    np.random.choice([4, 5], size=n_anomaly),           # rare types
    "severity":      np.random.randint(8, 11, size=n_anomaly),           # unusually high
    "duration_sec":  np.random.randint(200, 500, size=n_anomaly),        # very long
    "time_of_day":   np.random.choice([0, 1, 2, 3, 4], size=n_anomaly), # middle of night
    "frequency":     np.random.randint(50, 100, size=n_anomaly),         # spike in frequency
}

df_normal  = pd.DataFrame(normal_data)
df_anomaly = pd.DataFrame(anomaly_data)
df = pd.concat([df_normal, df_anomaly], ignore_index=True).sample(frac=1, random_state=42)

# Save CSV
os.makedirs("data", exist_ok=True)
df.to_csv("data/alarm_data.csv", index=False)
print(f"✅ CSV saved → data/alarm_data.csv  ({len(df)} rows)")

# ── 2. Train Isolation Forest ─────────────────────────────────────────────────
features = ["alarm_type", "severity", "duration_sec", "time_of_day", "frequency"]
X = df[features]

model = IsolationForest(
    n_estimators=100,
    contamination=0.1,   # we expect ~10% false alarms
    random_state=42
)
model.fit(X)
print("✅ Isolation Forest trained")

# ── 3. Save model ─────────────────────────────────────────────────────────────
os.makedirs("model", exist_ok=True)
joblib.dump(model, "model/isolation_forest.pkl")
print("✅ Model saved → model/isolation_forest.pkl")

# ── 4. Quick sanity check ─────────────────────────────────────────────────────
sample = pd.DataFrame([{
    "alarm_type": 1,
    "severity": 2,
    "duration_sec": 25,
    "time_of_day": 10,
    "frequency": 3
}])
result = model.predict(sample)
label = "✅ REAL alarm" if result[0] == 1 else "🚨 FALSE alarm"
print(f"\nSanity check on normal-looking alarm → {label}")

sample_bad = pd.DataFrame([{
    "alarm_type": 5,
    "severity": 10,
    "duration_sec": 450,
    "time_of_day": 2,
    "frequency": 80
}])
result_bad = model.predict(sample_bad)
label_bad = "✅ REAL alarm" if result_bad[0] == 1 else "🚨 FALSE alarm"
print(f"Sanity check on suspicious alarm     → {label_bad}")
