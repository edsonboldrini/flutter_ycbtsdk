import 'dart:async';
import 'dart:developer';

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

  StreamSubscription<List<ScanResult>>? _scanSubscription;
  StreamSubscription<Map>? _dataSubscription;
  List<ScanResult> scanResultsList = [];
  List<Map> dataList = [];

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> setup() async {
    try {
      await _flutterYcbtsdkPlugin.checkPermissions();
      startSubscriptions();
    } catch (e) {
      log(e.toString());
    }
  }

  startSubscriptions() {
    _scanSubscription =
        _flutterYcbtsdkPlugin.scanResultsStream.listen((scanResults) {
      scanResultsList = scanResults;
      setState(() {});
    });
  }

  @override
  void dispose() {
    _scanSubscription?.cancel();
    _dataSubscription?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('FlutterYCBTSDK example app'),
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
                      await _flutterYcbtsdkPlugin.disconnectDevice();
                      await _flutterYcbtsdkPlugin.startScan(60);
                    } catch (e) {
                      log(e.toString());
                    }
                  },
                  child: const Text('startScan BLE'),
                ),
                ElevatedButton(
                  onPressed: () async {
                    try {
                      await _flutterYcbtsdkPlugin.stopScan();
                    } catch (e) {
                      log(e.toString());
                    }
                  },
                  child: const Text('stopScan BLE'),
                ),
                IconButton(
                  iconSize: 20,
                  icon: const Icon(Icons.bluetooth),
                  onPressed: () async {
                    await _flutterYcbtsdkPlugin.disconnectDevice();
                    var connectionResponse = await _flutterYcbtsdkPlugin
                        .connectDevice('E0:6F:A7:A3:D9:D1');
                    log(connectionResponse.toString());
                    scanResultsList.add(
                      ScanResult(
                          mac: 'E0:6F:A7:A3:D9:D1', name: 'P 11', rssi: -70),
                    );
                    setState(() {});
                  },
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
              child: ListView.builder(
                itemCount: scanResultsList.length,
                itemBuilder: (context, index) {
                  ScanResult scanResult = scanResultsList[index];

                  return ListTile(
                    title: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text(scanResult.name),
                        Row(
                          mainAxisAlignment: MainAxisAlignment.end,
                          children: [
                            IconButton(
                              iconSize: 20,
                              icon: const Icon(Icons.bluetooth),
                              onPressed: () async {
                                await _flutterYcbtsdkPlugin.stopScan();
                                var connectionResponse =
                                    await _flutterYcbtsdkPlugin
                                        .connectDevice(scanResult.mac);
                                log(connectionResponse.toString());
                              },
                            ),
                            IconButton(
                              iconSize: 20,
                              icon: const Icon(Icons.bluetooth_disabled),
                              onPressed: () async {
                                await _flutterYcbtsdkPlugin.disconnectDevice();
                              },
                            ),
                            IconButton(
                              iconSize: 20,
                              icon: const Icon(Icons.arrow_forward),
                              onPressed: () async {
                                await _flutterYcbtsdkPlugin.healthHistoryData();
                                _dataSubscription = _flutterYcbtsdkPlugin
                                    .dataStream
                                    .listen((data) {
                                  dataList.add(data);
                                  setState(() {});
                                });
                              },
                            ),
                            IconButton(
                              iconSize: 20,
                              icon: const Icon(Icons.arrow_downward),
                              onPressed: () async {
                                await _flutterYcbtsdkPlugin.test();
                              },
                            ),
                          ],
                        ),
                      ],
                    ),
                    subtitle: Text('${scanResult.mac} ${scanResult.rssi}'),
                  );
                },
              ),
            ),
            Expanded(
              child: ListView.builder(
                itemCount: dataList.length,
                itemBuilder: (context, index) {
                  Map? map = dataList[index];

                  return ListTile(
                    title: Text(map.toString()),
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}
