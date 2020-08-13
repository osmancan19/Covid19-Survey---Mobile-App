import React from "react";
import { StyleSheet, Text, View, TouchableHighlight } from "react-native";

export default function NextButton({ disabled, onPress, text = "Next" }) {
  return (
    <View
      style={{
        flexDirection: "row",
        justifyContent: "flex-end",
        width: "100%",
        marginTop: 16,
      }}
    >
      <TouchableHighlight
        disabled={disabled}
        underlayColor="#fff"
        activeOpacity={0.6}
        onPress={onPress}
        style={{ borderRadius: 32 }}
        accessibilityLabel="next-button-highlight"
      >
        <View
          accessibilityLabel="next-button-view"
          style={[styles.button, disabled && styles.disabled]}
        >
          <Text
            accessibilityLabel="next-button-text"
            style={{ color: disabled ? "#ccc" : "#fff" }}
          >
            {text}
          </Text>
        </View>
      </TouchableHighlight>
    </View>
  );
}

const styles = StyleSheet.create({
  button: {
    borderRadius: 32,
    paddingHorizontal: 24,
    paddingVertical: 8,
    backgroundColor: "#3a86ff",
  },
  disabled: {
    backgroundColor: "#fff",
    borderColor: "#ccc",
    borderWidth: 1,
  },
});
