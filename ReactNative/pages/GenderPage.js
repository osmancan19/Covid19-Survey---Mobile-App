import React from "react";
import QuestionTitle from "./QuestionTitle";
import { StyleSheet, Text, View, TouchableOpacity } from "react-native";
import { useHistory } from "react-router-native";

export default function GenderPage({ handleChange, gender }) {
  const history = useHistory();
  function GenderButton({ text }) {
    return (
      <TouchableOpacity
        activeOpacity={0.3}
        style={[styles.gender, gender === text && styles.selected]}
        onPress={() => {
          handleChange("gender", text);
          history.push("/lifestyle");
        }}
        accessibilityLabel={`gender-${text}`}
      >
        <Text style={{ color: gender === text ? "#fff" : "#3a86ff" }}>
          {text}
        </Text>
      </TouchableOpacity>
    );
  }

  return (
    <>
      <QuestionTitle title="What is your gender?" />
      <View
        style={{
          width: "100%",
          alignItems: "flex-end",
        }}
      >
        <GenderButton text="Male" />
        <GenderButton text="Female" />
        <GenderButton text="Non-Binary" />
      </View>
    </>
  );
}

const styles = StyleSheet.create({
  gender: {
    borderColor: "#3a86ff",
    borderWidth: 1,
    borderRadius: 32,
    paddingHorizontal: 24,
    paddingVertical: 8,
    backgroundColor: "#fff",
    marginBottom: 8,
  },
  selected: {
    backgroundColor: "#3a86ff",
  },
});
