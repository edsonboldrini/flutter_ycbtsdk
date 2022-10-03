import 'dart:async';

import 'package:flutter/material.dart';
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
  final _flutterYcbtsdkPlugin = FlutterYcbtsdk.instance;

  @override
  void initState() {
    super.initState();
    setup();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> setup() async {
    try {
      await _flutterYcbtsdkPlugin.checkPermissions();
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
                          title: Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: [
                              Text(scanResult.name),
                              Row(
                                mainAxisAlignment: MainAxisAlignment.end,
                                children: [
                                  IconButton(
                                    icon: const Icon(Icons.bluetooth),
                                    onPressed: () async {
                                      await _flutterYcbtsdkPlugin
                                          .connectDevice(scanResult.mac);
                                    },
                                  ),
                                  IconButton(
                                    icon: const Icon(Icons.arrow_forward),
                                    onPressed: () async {
                                      await _flutterYcbtsdkPlugin
                                          .startMeasurement();
                                    },
                                  ),
                                  // IconButton(
                                  //   icon: const Icon(Icons.arrow_back),
                                  //   onPressed: () async {
                                  //     await _flutterYcbtsdkPlugin.stopEcgTest();
                                  //   },
                                  // ),
                                  IconButton(
                                    icon: const Icon(Icons.close),
                                    onPressed: () async {
                                      await _flutterYcbtsdkPlugin
                                          .disconnectDevice();
                                    },
                                  ),
                                ],
                              ),
                            ],
                          ),
                          subtitle:
                              Text('${scanResult.mac} ${scanResult.rssi}'),
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
