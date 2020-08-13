import React, { useMemo } from "react";
import { StyleSheet, Text, View, TextInput } from "react-native";
import { useHistory } from "react-router-native";
import QuestionTitle from "./QuestionTitle";
import NextButton from "./NextButton";

export default function NamePage({ handleChange, name, surname, hasError }) {
  const history = useHistory();

  function handleOnPress() {
    history.push("/birthday");
  }

  return (
    <>
      <QuestionTitle title="What's your name?" />
      <View
        style={{
          width: "100%",
          paddingBottom: 8,
          flexDirection: "row",
          justifyContent: "space-between",
        }}
      >
        <Text
          style={{
            fontWeight: "bold",
            width: "50%",
            marginRight: 12,
            marginLeft: 4,
          }}
        >
          Name
        </Text>
        <Text style={{ fontWeight: "bold", width: "50%" }}>Surname</Text>
      </View>
      <View
        style={{
          width: "100%",
          paddingBottom: 8,
          flexDirection: "row",
          justifyContent: "space-between",
        }}
      >
        <TextInput
          style={styles.input}
          autoCapitalize="words"
          onChangeText={(text) => handleChange("name", text)}
          value={name}
          accessibilityLabel="name-input"
        />
        <TextInput
          autoCapitalize="words"
          style={styles.input}
          onChangeText={(text) => handleChange("surname", text)}
          value={surname}
          accessibilityLabel="surname-input"
        />
      </View>
      <NextButton disabled={hasError} onPress={handleOnPress} />
    </>
  );
}

const styles = StyleSheet.create({
  input: {
    height: 40,
    borderColor: "gray",
    borderWidth: 1,
    borderRadius: 24,
    width: "50%",
    marginRight: 12,
    paddingHorizontal: 16,
  },
});
