import React, { useState, useEffect } from "react";
import { Text, View, TouchableOpacity } from "react-native";
import DateTimePicker from "@react-native-community/datetimepicker";
import QuestionTitle from "./QuestionTitle";
import NextButton from "./NextButton";
import { useHistory } from "react-router-native";

export default function BirthdayPage({ handleChange, birthdate, hasError }) {
  const history = useHistory();
  const [show, setShow] = useState(false);
  const [error, setError] = useState(null);

  const onChange = (event, selectedDate) => {
    const currentDate = selectedDate || birthdate;
    if (new Date().getFullYear() - currentDate.getFullYear() < 13) {
      setError(
        "You must be at least 13 years old to participate in the survey."
      );
    } else {
      setError(null);
    }
    setShow(Platform.OS === "ios");
    handleChange("birthdate", currentDate);
  };

  function getErrorText() {
    if (Platform.OS === "ios") {
      return (
        <Text
          testID="error-text"
          style={{
            color: "red",
            padding: 8,
            paddingBottom: 0,
          }}
        >
          {error}
        </Text>
      );
    }
    return (
      <Text
        accessibilityLabel="error-text"
        style={{
          color: "red",
          padding: 8,
          paddingBottom: 0,
        }}
      >
        {error}
      </Text>
    );
  }

  return (
    <>
      <QuestionTitle title="What's your date of birth?" />
      <TouchableOpacity
        style={{
          height: 40,
          width: "100%",
        }}
        onPress={() => setShow(true)}
        accessibilityLabel="birthday-button"
      >
        <View
          style={{
            height: 40,
            width: "100%",
            borderColor: "gray",
            borderWidth: 1,
            borderRadius: 24,
            paddingVertical: 8,
            paddingHorizontal: 16,
            marginRight: 12,
          }}
        >
          <Text style={{ color: "#000" }}>{birthdate.toDateString()}</Text>
        </View>
      </TouchableOpacity>
      {show && (
        <DateTimePicker
          accessibilityLabel="date-picker"
          value={birthdate}
          mode="date"
          is24Hour={true}
          display="default"
          onChange={onChange}
        />
      )}
      {error != null && getErrorText()}
      <NextButton
        disabled={hasError}
        onPress={() => {
          history.push("/city");
        }}
      />
    </>
  );
}
