import React, { useState } from "react";
import { Text, TextInput, Platform } from "react-native";
import QuestionTitle from "./QuestionTitle";
import NextButton from "./NextButton";
import { useHistory } from "react-router-native";

export default function LifestylePage({ handleChange, lifestyle, finished }) {
  const history = useHistory();
  return (
    <>
      <QuestionTitle title="How has your lifestyle changed since the COVID-19 Outbreak started?" />
      <TextInput
        accessibilityLabel="lifestyle-input"
        value={lifestyle}
        onChangeText={(text) => handleChange("lifestyle", text)}
        multiline
        numberOfLines={4}
        style={{
          borderColor: "gray",
          borderWidth: 1,
          borderRadius: 24,
          width: "100%",
          paddingHorizontal: 16,
          height: 80,
        }}
      />
      <Text
        style={{
          padding: 8,
          paddingBottom: 0,
          fontWeight: "100",
        }}
      >
        At least 10 characters
      </Text>
      <NextButton
        disabled={!finished}
        onPress={() => {
          history.push("/finish");
        }}
        text="Submit"
      />
    </>
  );
}
