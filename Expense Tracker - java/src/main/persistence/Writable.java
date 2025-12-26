package persistence;

import org.json.JSONObject;

// borrowed from JasonSerialisationDemo
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
