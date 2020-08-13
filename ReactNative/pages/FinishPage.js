import React from "react";
import QuestionTitle from "./QuestionTitle";
import NextButton from "./NextButton";
import { useHistory } from "react-router-native";

export default function FinishPage({ resetForm }) {
  const history = useHistory();
  return (
    <>
      <QuestionTitle title="You have completed the survey" />
      <NextButton
        text="Take the survey again"
        onPress={() => {
          resetForm();
          history.push("/");
        }}
      />
    </>
  );
}
