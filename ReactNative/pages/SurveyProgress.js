import React, { useState, useEffect } from "react";
import {
  StyleSheet,
  View,
  Text,
  TouchableOpacity,
  Platform,
} from "react-native";
import { Ionicons } from "@expo/vector-icons";
import { useHistory } from "react-router-native";
const descriptions = ["Name", "Age", "City", "Gender", "Life"];
const links = ["/", "/birthday", "/city", "/gender", "/lifestyle"];

export default function SurveyProgress({ correctSteps, current }) {
  const history = useHistory();
  const [[dirtySteps], setDirtySteps] = useState([new Set()]);

  useEffect(() => {
    if (current === 5) {
      setDirtySteps([new Set()]);
    } else {
      setDirtySteps([dirtySteps.add(current)]);
    }
  }, [current]);

  return (
    <View style={styles.container}>
      {correctSteps.map((isValid, i) => {
        return (
          <View
            key={i}
            style={{
              flexDirection: "row",
              alignItems: "center",
            }}
          >
            <View
              style={{
                alignItems: "center",
                marginTop: 20,
              }}
            >
              <TouchableOpacity
                onPress={() => history.push(links[i])}
                accessibilityLabel={`survey-step-${i}`}
              >
                <View
                  key={i}
                  style={[
                    styles.step,
                    current === i && styles.current,
                    current !== i &&
                      dirtySteps.has(i) &&
                      !isValid &&
                      styles.error,
                  ]}
                >
                  {isValid && (
                    <Ionicons
                      accessibilityLabel="valid-icon"
                      name={
                        Platform.OS === "ios" ? "ios-checkmark" : "md-checkmark"
                      }
                      size={20}
                      color={current === i ? "#fff" : "#3a86ff"}
                    />
                  )}
                  {!isValid && dirtySteps.has(i) && (
                    <Ionicons
                      accessibilityLabel="invalid-icon"
                      name={Platform.OS === "ios" ? "ios-close" : "md-close"}
                      size={20}
                      color="#fff"
                    />
                  )}
                </View>
              </TouchableOpacity>
              <Text style={{ fontSize: 12 }}>{descriptions[i]}</Text>
            </View>
            {i !== correctSteps.length - 1 && <View style={styles.line} />}
          </View>
        );
      })}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    paddingVertical: 32,
    flexDirection: "row",
    justifyContent: "center",
    alignItems: "center",
  },
  step: {
    width: 30,
    height: 30,
    borderRadius: 999,
    borderColor: "#3a86ff",
    borderWidth: 1,
    alignItems: "center",
    justifyContent: "center",
    marginHorizontal: 5,
  },
  current: {
    backgroundColor: "#3a86ff",
  },
  error: {
    backgroundColor: "#ff0033",
    borderColor: "#ff0033",
  },
  line: {
    width: 30,
    height: 1,
    borderColor: "#3a86ff",
    borderWidth: 0.5,
  },
});
