import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_ycbtsdk/flutter_ycbtsdk.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  final _flutterYcbtsdkPlugin = FlutterYcbtsdk.instance;

  @override
  void initState() {
    super.initState();
    initPlatformState();
    initPlugin();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion = await _flutterYcbtsdkPlugin.getPlatformVersion() ??
          'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  Future<void> initPlugin() async {
    try {
      await _flutterYcbtsdkPlugin.initPlugin();

      _flutterYcbtsdkPlugin.scanResults.listen((event) {
        print(event);
      });
    } catch (e) {
      // log(e.toString());
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: [
            const SizedBox(
              height: 10,
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                ElevatedButton(
                  onPressed: () async {
                    try {
                      await _flutterYcbtsdkPlugin.startScan();
                    } catch (e) {
                      // log(e.toString());
                    }
                  },
                  child: const Text('startScan BLE'),
                ),
                ElevatedButton(
                  onPressed: () async {
                    try {
                      await _flutterYcbtsdkPlugin.stopScan();
                    } catch (e) {
                      // log(e.toString());
                    }
                  },
                  child: const Text('stopScan BLE'),
                ),
              ],
            ),
            const SizedBox(
              height: 10,
            ),
            const Text('scanResults'),
            const SizedBox(
              height: 10,
            ),
            Expanded(
              child: StreamBuilder<List<ScanResult>>(
                stream: _flutterYcbtsdkPlugin.scanResults,
                initialData: const [],
                builder: (c, snapshot) {
                  if (snapshot.hasData) {
                    List<ScanResult> scanResults = snapshot.data!;

                    return ListView.builder(
                      itemCount: scanResults.length,
                      itemBuilder: (context, index) {
                        ScanResult scanResult = scanResults[index];

                        return ListTile(
                          title: Text(scanResult.name),
                          subtitle:
                              Text('${scanResult.mac} ${scanResult.rssi}'),
                          onTap: () {
                            //   Navigator.of(context)
                            //     .push(MaterialPageRoute(builder: (context) {
                            //   r.device.connect();
                            //   return DeviceScreen(device: r.device);
                            // }));
                          },
                        );
                      },
                    );
                  }

                  return const SizedBox();
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}
