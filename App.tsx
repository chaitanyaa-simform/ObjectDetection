/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React, { useState } from 'react';
import {
  Alert,
  Image,
  NativeModules,
  SafeAreaView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import {
  ImagePickerResponse,
  launchCamera,
  launchImageLibrary,
} from 'react-native-image-picker';

function App(): JSX.Element {
  const [image, setImage] = useState<ImagePickerResponse | null>();
  const [result, setResult] = useState<string>('');

  const { CustomObjectDetectionModule, ObjectDetectionModule } = NativeModules;

  console.log(result, '>>>> result');

  const chooseFile = async () => {
    launchImageLibrary({ mediaType: 'photo' }, async response => {
      if (!response.didCancel) {
        if (response.assets && response.assets.length > 0) {
          try {
            const res =
              await CustomObjectDetectionModule.startCustomObjectDetection(
                response.assets[0].uri,
              );
            console.log(res, '<<<res');
            setImage(response.assets[0].uri);
            setResult(res);
          } catch (error) {
            Alert.alert('Error', 'No Object Detected', [{ text: 'OK' }]);
            setImage({});
            setResult('');
          }
        }
      } else {
        console.log(response.errorMessage);
      }
    });
  };

  const openCamera = () => {
    launchCamera({ mediaType: 'photo' }, async response => {
      if (!response.didCancel) {
        if (response.assets && response.assets.length > 0) {
          try {
            const res =
              await CustomObjectDetectionModule.startCustomObjectDetection(
                response.assets[0].uri,
              );
            setImage(response.assets[0].uri);
            setResult(res);
          } catch (error) {
            Alert.alert('Error', 'No Object Detected', [{ text: 'OK' }]);
            setImage({});
            setResult('');
          }
        }
      } else {
        console.log(response.errorMessage);
      }
    });
  };

  return (
    <SafeAreaView style={styles.container}>
      <Text style={styles.textStyle}>Object Detection</Text>
      <Image
        style={styles.imageStyle}
        source={{
          uri: image,
        }}
      />
      <Text style={styles.textStyle}>{`Result: ${result}`}</Text>
      <View style={styles.buttonContainer}>
        <TouchableOpacity style={styles.buttonStyle} onPress={chooseFile}>
          <Text style={styles.buttonLabelStyle}>Launch gallery</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.buttonStyle} onPress={openCamera}>
          <Text style={styles.buttonLabelStyle}>Launch Camera</Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'white',
    alignContent: 'center',
    padding: 20,
    justifyContent: 'space-between',
  },
  buttonStyle: {
    height: '35%',
    backgroundColor: '#D15060',
    alignItems: 'center',
    justifyContent: 'center',
    paddingHorizontal: 20,
    borderRadius: 7,
  },
  textStyle: {
    color: 'black',
    textAlign: 'center',
    fontSize: 22,
    fontWeight: '600',
  },
  imageStyle: {
    height: '70%',
    width: '80%',
    resizeMode: 'contain',
    alignSelf: 'center',
  },
  buttonContainer: {
    alignItems: 'center',
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingHorizontal: 10,
  },
  buttonLabelStyle: { fontSize: 15, fontWeight: '500', color: '#FFFFFF' },
});

export default App;
