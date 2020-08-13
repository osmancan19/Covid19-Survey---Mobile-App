import React from "react";
import { StyleSheet, Text, View } from "react-native";

export default function QuestionTitle({ title }) {
  if (Platform.OS === "ios") {
    return (
      <>
        <View style={styles.titleContainer}>
          <Text testID="question-title" style={styles.titleText}>
            {title}
          </Text>
        </View>
      </>
    );
  }
  return (
    <>
      <View style={styles.titleContainer}>
        <Text
          accessible={true}
          accessibilityLabel="question-title"
          style={styles.titleText}
        >
          {title}
        </Text>
      </View>
    </>
  );
}

const styles = StyleSheet.create({
  titleContainer: {
    width: "70%",
    paddingBottom: 16,
  },
  titleText: {
    fontSize: 24,
  },
});
