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
      // print(scanResults.toString());
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
                    var connectionResponse = await _flutterYcbtsdkPlugin
                        .connectDevice('E0:6F:A7:A3:D9:D1');
                    log(connectionResponse.toString());
                    
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
              child: StreamBuilder<List<ScanResult>>(
                stream: _flutterYcbtsdkPlugin.scanResultsStream,
                initialData: const [],
                builder: (c, snapshot) {
                  if (snapshot.hasData) {
                    List<ScanResult> scanResults = snapshot.data!;

                    return ListView.builder(
                      itemCount: scanResults.length,
                      itemBuilder: (context, index) {
                        ScanResult? scanResult = scanResults[index];

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
                                      var connectionResponse =
                                          await _flutterYcbtsdkPlugin
                                              .connectDevice(scanResult.mac);
                                      log(connectionResponse.toString());
                                    },
                                  ),
                                  IconButton(
                                    iconSize: 20,
                                    icon: const Icon(Icons.arrow_forward),
                                    onPressed: () async {
                                      await _flutterYcbtsdkPlugin
                                          .healthHistoryData();
                                      _dataSubscription = _flutterYcbtsdkPlugin
                                          .dataStream
                                          .listen((data) {
                                        // print(data.toString());
                                        dataList.add(data);
                                        setState(() {});
                                      });
                                    },
                                  ),
                                  IconButton(
                                    iconSize: 20,
                                    icon: const Icon(Icons.arrow_forward),
                                    onPressed: () async {
                                      await _flutterYcbtsdkPlugin
                                          .startMeasurement(0x02, 0x04);
                                      _dataSubscription = _flutterYcbtsdkPlugin
                                          .dataStream
                                          .listen((data) {
                                        // print(data.toString());
                                        dataList.add(data);
                                        setState(() {});
                                      });
                                    },
                                  ),
                                  // IconButton(
                                  //   icon: const Icon(Icons.arrow_back),
                                  //   onPressed: () async {
                                  //     await _flutterYcbtsdkPlugin.stopEcgTest();
                                  //   },
                                  // ),
                                  IconButton(
                                    iconSize: 20,
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
