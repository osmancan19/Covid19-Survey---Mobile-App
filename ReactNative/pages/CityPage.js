import React from "react";
import { Platform } from "react-native";
import { Picker } from "@react-native-community/picker";
import QuestionTitle from "./QuestionTitle";
import NextButton from "./NextButton";
import { useHistory } from "react-router-native";

const cities = [
  "Adana",
  "Adıyaman",
  "Afyonkarahisar",
  "Ağrı",
  "Aksaray",
  "Amasya",
  "Ankara",
  "Antalya",
  "Ardahan",
  "Artvin",
  "Aydın",
  "Balıkesir",
  "Bartın",
  "Batman",
  "Bayburt",
  "Bilecik",
  "Bingöl",
  "Bitlis",
  "Bolu",
  "Burdur",
  "Bursa",
  "Çanakkale",
  "Çankırı",
  "Çorum",
  "Denizli",
  "Diyarbakır",
  "Düzce",
  "Edirne",
  "Elazığ",
  "Erzincan",
  "Erzurum",
  "Eskişehir",
  "Gaziantep",
  "Giresun",
  "Gümüşhane",
  "Hakkâri",
  "Hatay",
  "Iğdır",
  "Isparta",
  "İstanbul",
  "İzmir",
  "Kahramanmaraş",
  "Karabük",
  "Karaman",
  "Kars",
  "Kastamonu",
  "Kayseri",
  "Kilis",
  "Kırıkkale",
  "Kırklareli",
  "Kırşehir",
  "Kocaeli",
  "Konya",
  "Kütahya",
  "Malatya",
  "Manisa",
  "Mardin",
  "Mersin",
  "Muğla",
  "Muş",
  "Nevşehir",
  "Niğde",
  "Ordu",
  "Osmaniye",
  "Rize",
  "Sakarya",
  "Samsun",
  "Şanlıurfa",
  "Siirt",
  "Sinop",
  "Sivas",
  "Şırnak",
  "Tekirdağ",
  "Tokat",
  "Trabzon",
  "Tunceli",
  "Uşak",
  "Van",
  "Yalova",
  "Yozgat",
  "Zonguldak",
];

export default function CityPage({ handleChange, city, hasError }) {
  const history = useHistory();
  return (
    <>
      <QuestionTitle title="Which city are you from?" />
      <Picker
        accessibilityLabel="city-picker"
        selectedValue={city}
        prompt="Select your city"
        style={{
          height: 50,
          width: "100%",
          marginBottom: Platform.OS === "ios" ? 150 : 0,
        }}
        onValueChange={(itemValue) => handleChange("city", itemValue)}
      >
        <Picker.Item
          key={"placeholder"}
          label={"Please select a city"}
          value={""}
        />
        {cities.map((city, id) => (
          <Picker.Item
            key={id}
            label={city}
            value={city}
            accessibilityLabel={`picker-item-${city}`}
          />
        ))}
      </Picker>
      <NextButton disabled={hasError} onPress={() => history.push("/gender")} />
    </>
  );
}
