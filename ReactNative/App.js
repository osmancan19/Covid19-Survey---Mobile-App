import { StatusBar } from "expo-status-bar";
import React, { useState, useMemo, useEffect } from "react";
import {
  StyleSheet,
  View,
  Text,
  KeyboardAvoidingView,
  Platform,
  Keyboard,
} from "react-native";
import { NativeRouter, Route, Switch, useLocation } from "react-router-native";

import { setCustomText } from "react-native-global-props";
import { useFonts, Inter_300Light } from "@expo-google-fonts/inter";
import NamePage from "./pages/NamePage";
import BirthdayPage from "./pages/BirthdayPage";
import CityPage from "./pages/CityPage";
import GenderPage from "./pages/GenderPage";
import LifestylePage from "./pages/LifestylePage";
import FinishPage from "./pages/FinishPage";
import SurveyProgress from "./pages/SurveyProgress";

const customTextProps = {
  style: {
    fontFamily: "Inter_300Light",
  },
};

export default function App() {
  let [fontsLoaded] = useFonts({
    Inter_300Light,
  });

  if (!fontsLoaded) {
    return <Text>Loading</Text>;
  }
  setCustomText(customTextProps);

  return (
    <NativeRouter>
      <Content />
    </NativeRouter>
  );
}

function Content() {
  const location = useLocation();
  const [isKeyboardVisible, setKeyboardVisible] = useState(false);
  const [formData, setFormData] = useState({
    name: "",
    surname: "",
    birthdate: new Date(),
    city: "",
    gender: "",
    lifestyle: "",
  });
  const correctSteps = useMemo(() => {
    return {
      name: formData.name !== "" && formData.surname !== "",
      age: new Date().getFullYear() - formData.birthdate.getFullYear() > 13,
      city: formData.city !== "",
      gender: formData.gender !== "",
      lifestyle: formData.lifestyle.length >= 10,
    };
  }, [formData]);

  useEffect(() => {
    const keyboardDidShowListener = Keyboard.addListener(
      "keyboardDidShow",
      () => {
        setKeyboardVisible(true); // or some other action
      }
    );
    const keyboardDidHideListener = Keyboard.addListener(
      "keyboardDidHide",
      () => {
        setKeyboardVisible(false); // or some other action
      }
    );

    return () => {
      keyboardDidHideListener.remove();
      keyboardDidShowListener.remove();
    };
  }, []);

  function handleChange(name, value) {
    setFormData((formData) => ({
      ...formData,
      [name]: value,
    }));
  }

  function getProgress() {
    switch (location.pathname) {
      case "/":
        return 0;
      case "/birthday":
        return 1;
      case "/city":
        return 2;
      case "/gender":
        return 3;
      case "/lifestyle":
        return 4;
      case "/finish":
        return 5;
    }
  }

  return (
    <View style={styles.main}>
      <View style={styles.bar}>
        <View
          style={{
            display: isKeyboardVisible ? "none" : "flex",
            justifyContent: "center",
            alignItems: "center",
          }}
        >
          <Text
            style={{
              fontSize: 24,
            }}
          >
            COVID-19 Survey
          </Text>
          <View
            style={{
              flexDirection: "row",
              justifyContent: "space-between",
              width: "100%",
            }}
          >
            <SurveyProgress
              correctSteps={Object.values(correctSteps)}
              current={getProgress()}
            />
          </View>
        </View>
      </View>
      <KeyboardAvoidingView
        style={styles.container}
        behavior={Platform.OS == "ios" ? "padding" : "height"}
      >
        <Switch>
          <Route exact path="/">
            <NamePage
              handleChange={handleChange}
              name={formData.name}
              surname={formData.surname}
              hasError={!correctSteps.name}
            />
          </Route>
          <Route path="/birthday">
            <BirthdayPage
              handleChange={handleChange}
              birthdate={formData.birthdate}
              hasError={!correctSteps.age}
            />
          </Route>
          <Route path="/city">
            <CityPage
              handleChange={handleChange}
              city={formData.city}
              hasError={!correctSteps.city}
            />
          </Route>
          <Route path="/gender">
            <GenderPage handleChange={handleChange} gender={formData.gender} />
          </Route>
          <Route path="/lifestyle">
            <LifestylePage
              handleChange={handleChange}
              lifestyle={formData.lifestyle}
              finished={Object.values(correctSteps).every((step) => step)}
            />
          </Route>
          <Route path="/finish">
            <FinishPage
              resetForm={() =>
                setFormData({
                  name: "",
                  surname: "",
                  birthdate: new Date(),
                  city: "",
                  gender: "",
                  lifestyle: "",
                })
              }
            />
          </Route>
        </Switch>
      </KeyboardAvoidingView>
    </View>
  );
}

const styles = StyleSheet.create({
  main: {
    flex: 1,
    height: "100%",
  },
  bar: {
    flex: 1,
    alignItems: "center",
    justifyContent: "flex-end",
    padding: 60,
    paddingTop: 0,
    width: "100%",
  },
  container: {
    flex: 2,
    justifyContent: "flex-end",
    marginBottom: 32,
    paddingHorizontal: 32,
    width: "100%",
  },
});
