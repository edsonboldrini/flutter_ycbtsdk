import 'dart:async';
import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:flutter_ycbtsdk/flutter_ycbtsdk.dart';

void main() {
  runApp(const HomePage());
}

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  final _flutterYcbtsdkPlugin = FlutterYcbtsdk.instance;

  @override
  void initState() {
    super.initState();
    init();
  }

  StreamSubscription<List<ScanResult>>? _scanSubscription;
  StreamSubscription<Map>? _dataSubscription;
  List<ScanResult> scanResultsList = [];
  List<Map> dataList = [];

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> init() async {
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

  Future _startScan() async {
    try {
      await _flutterYcbtsdkPlugin.disconnectDevice();
      await _flutterYcbtsdkPlugin.startScan(60);
    } catch (e) {
      log(e.toString());
    }
  }

  Future _stopScan() async {
    try {
      await _flutterYcbtsdkPlugin.stopScan();
    } catch (e) {
      log(e.toString());
    }
  }

  Future _forceConnect(ScanResult scanResult) async {
    _forceDisconnect();
    var connectionResponse =
        await _flutterYcbtsdkPlugin.connectDevice('E0:6F:A7:A3:D9:D1');
    log(connectionResponse.toString());
    scanResultsList.add(scanResult);
    setState(() {});
  }

  Future _forceDisconnect() async {
    scanResultsList.clear();
    setState(() {});
    await _flutterYcbtsdkPlugin.disconnectDevice();
  }

  Future _connectDevice(ScanResult scanResult) async {
    _stopScan();
    var connectionResponse =
        await _flutterYcbtsdkPlugin.connectDevice(scanResult.mac);
    log(connectionResponse.toString());
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
        body: Builder(builder: (context) {
          return Column(
            children: [
              const SizedBox(
                height: 10,
              ),
              Container(
                padding: const EdgeInsets.all(16),
                child: Column(
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        ElevatedButton(
                          child: const Text('startScan BLE'),
                          onPressed: () async {
                            await _startScan();
                          },
                        ),
                        ElevatedButton(
                          child: const Text('stopScan BLE'),
                          onPressed: () async {
                            await _stopScan();
                          },
                        ),
                      ],
                    ),
                    const SizedBox(
                      height: 10,
                    ),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        ElevatedButton(
                          child: const Text("forceConnectDevice"),
                          onPressed: () async {
                            final scanResult = ScanResult(
                              mac: 'E0:6F:A7:A3:D9:D1',
                              name: 'P 11',
                              rssi: -70,
                            );

                            await _forceConnect(scanResult);

                            if (!mounted) return;
                            Navigator.of(context).push(
                              MaterialPageRoute(
                                builder: (BuildContext context) =>
                                    DeviceDetailsPage(
                                  device: scanResult,
                                ),
                              ),
                            );
                          },
                        ),
                        ElevatedButton(
                          child: const Text("forceDisconnectDevice"),
                          onPressed: () async {
                            await _forceDisconnect();
                          },
                        ),
                      ],
                    ),
                  ],
                ),
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
                  itemBuilder: (_, index) {
                    ScanResult scanResult = scanResultsList[index];

                    return ListTile(
                      title: Text(scanResult.name),
                      subtitle: Text('${scanResult.mac} ${scanResult.rssi}'),
                      onTap: (() async {
                        await _connectDevice(scanResult);

                        if (!mounted) return;
                        Navigator.of(context).push(
                          MaterialPageRoute(
                            builder: (BuildContext context) =>
                                DeviceDetailsPage(
                              device: scanResult,
                            ),
                          ),
                        );
                      }),
                    );
                  },
                ),
              ),
            ],
          );
        }),
      ),
    );
  }
}

class DeviceDetailsPage extends StatefulWidget {
  final ScanResult device;

  const DeviceDetailsPage({super.key, required this.device});

  @override
  State<DeviceDetailsPage> createState() => DeviceDetailsStatePage();
}

class DeviceDetailsStatePage extends State<DeviceDetailsPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Device details page'),
      ),
      body: Center(
        child: Column(children: [
          const SizedBox(
            height: 10,
          ),
          Text('name: ${widget.device.name}'),
          Text('mac: ${widget.device.mac}'),
          Text('rssi: ${widget.device.rssi}'),
          // Expanded(
          //   child: ListView.builder(
          //     itemCount: dataList.length,
          //     itemBuilder: (context, index) {
          //       Map? map = dataList[index];

          //       return ListTile(
          //         title: Text(map.toString()),
          //       );
          //     },
          //   ),
          // ),
          // IconButton(
          //   iconSize: 20,
          //   icon: const Icon(Icons.bluetooth),
          //   onPressed: () async {
          //     await _flutterYcbtsdkPlugin.stopScan();
          //     var connectionResponse =
          //         await _flutterYcbtsdkPlugin.connectDevice(scanResult.mac);
          //     log(connectionResponse.toString());
          //   },
          // ),
          // IconButton(
          //   iconSize: 20,
          //   icon: const Icon(Icons.bluetooth_disabled),
          //   onPressed: () async {
          //     await _flutterYcbtsdkPlugin.disconnectDevice();
          //     scanResultsList.clear();
          //     setState(() {});
          //   },
          // ),
          // IconButton(
          //   iconSize: 20,
          //   icon: const Icon(Icons.arrow_forward),
          //   onPressed: () async {
          //     await _flutterYcbtsdkPlugin.healthHistoryData();
          //     _dataSubscription = _flutterYcbtsdkPlugin.dataStream.listen((data) {
          //       dataList.add(data);
          //       setState(() {});
          //     });
          //   },
          // ),
          // IconButton(
          //   iconSize: 20,
          //   icon: const Icon(Icons.arrow_downward),
          //   onPressed: () async {
          //     await _flutterYcbtsdkPlugin.test();
          //   },
          // ),
        ]),
      ),
    );
  }
}
