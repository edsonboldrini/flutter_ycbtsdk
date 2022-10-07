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

  StreamSubscription<List<ScanResult>>? _scanSubscription;
  List<ScanResult> _scanResultsList = [];

  @override
  void initState() {
    super.initState();
    init();
  }

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
      _scanResultsList = scanResults;
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
    _scanResultsList.add(scanResult);
    setState(() {});
  }

  Future _forceDisconnect() async {
    _scanResultsList.clear();
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
              Padding(
                padding: const EdgeInsets.all(16.0),
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
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        ElevatedButton(
                          style: ButtonStyle(
                            backgroundColor: MaterialStateColor.resolveWith(
                                (states) => Colors.red),
                          ),
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
                          style: ButtonStyle(
                            backgroundColor: MaterialStateColor.resolveWith(
                                (states) => Colors.red),
                          ),
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
              const Text('scanResults:'),
              const SizedBox(
                height: 10,
              ),
              Expanded(
                child: ListView.builder(
                  itemCount: _scanResultsList.length,
                  itemBuilder: (_, index) {
                    ScanResult scanResult = _scanResultsList[index];

                    return ListTile(
                      leading: const Icon(Icons.bluetooth),
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
  final _flutterYcbtsdkPlugin = FlutterYcbtsdk.instance;

  StreamSubscription<WristbandData>? _dataSubscription;
  final List<WristbandData> _dataList = [];

  @override
  void initState() {
    super.initState();
    init();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> init() async {
    try {
      startSubscriptions();
    } catch (e) {
      log(e.toString());
    }
  }

  startSubscriptions() {
    _dataSubscription = _flutterYcbtsdkPlugin.dataStream.listen((data) {
      _dataList.add(data);
      setState(() {});
    });
  }

  @override
  void dispose() {
    _dataSubscription?.cancel();
    super.dispose();
  }

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
          Padding(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    ElevatedButton(
                      child: const Text('connectState'),
                      onPressed: () async {
                        await _flutterYcbtsdkPlugin.connectState();
                      },
                    ),
                    ElevatedButton(
                      child: const Text('resetQueue'),
                      onPressed: () async {
                        await _flutterYcbtsdkPlugin.resetQueue();
                      },
                    ),
                  ],
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    ElevatedButton(
                      child: const Text('startEcgTest'),
                      onPressed: () async {
                        await _flutterYcbtsdkPlugin.startEcgTest();
                      },
                    ),
                    ElevatedButton(
                      child: const Text('stopEcgTest'),
                      onPressed: () async {
                        await _flutterYcbtsdkPlugin.stopEcgTest();
                      },
                    ),
                  ],
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    ElevatedButton(
                      child: const Text('getHealthHistory'),
                      onPressed: () async {
                        await _flutterYcbtsdkPlugin.healthHistoryData();
                      },
                    ),
                    ElevatedButton(
                      child: const Text('deleteHealthHistory'),
                      onPressed: () async {
                        await _flutterYcbtsdkPlugin.deleteHealthHistoryData();
                      },
                    ),
                  ],
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    ElevatedButton(
                      style: ButtonStyle(
                        backgroundColor: MaterialStateColor.resolveWith(
                            (states) => Colors.red),
                      ),
                      child: const Text('test'),
                      onPressed: () async {
                        await _flutterYcbtsdkPlugin.test();
                      },
                    ),
                    ElevatedButton(
                      style: ButtonStyle(
                        backgroundColor: MaterialStateColor.resolveWith(
                            (states) => Colors.red),
                      ),
                      child: const Text('clearDataStreamed'),
                      onPressed: () async {
                        _dataList.clear();
                        setState(() {});
                      },
                    ),
                  ],
                ),
              ],
            ),
          ),
          const Text('dataStreamed:'),
          const SizedBox(
            height: 10,
          ),
          Expanded(
            child: ListView.builder(
              itemCount: _dataList.length,
              itemBuilder: (context, index) {
                WristbandData? data = _dataList[index];

                return ListTile(
                  title: Text(data.toString()),
                );
              },
            ),
          ),
        ]),
      ),
    );
  }
}
