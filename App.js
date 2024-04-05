import { StatusBar } from 'expo-status-bar';
import { StyleSheet, Text, View, Button, Pressable, NativeModules } from 'react-native';

export default function App() {
  const {ReactConnectionModule} = NativeModules;

  const onPress = () => {
    ReactConnectionModule.callStopEvent();
  };

  return (
    <View style={styles.container}>
      <Text style={styles.text}>Call pending...</Text>
      <Pressable style={styles.button} onPress={onPress}>
        <Text style={styles.buttonTitle}>Stop active call</Text>
      </Pressable>
      <StatusBar style="auto" />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center'
  },
  text: {
    padding: 12
  },
  button: {
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 12,
    paddingHorizontal: 32,
    borderRadius: 4,
    elevation: 3,
    backgroundColor: '#E54B4D'
  },
  buttonTitle: {
    fontSize: 16,
    lineHeight: 21,
    fontWeight: 'bold',
    letterSpacing: 0.25,
    color: 'white'
  }
});
