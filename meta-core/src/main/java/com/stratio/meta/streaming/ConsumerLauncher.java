/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.streaming;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsumerLauncher {
  public static void main(String[] args){
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter Kafka topic name: ");
    String topic = scanner.nextLine();

    List<String> results = new ArrayList<>();

    StreamingConsumer consumer = new StreamingConsumer(topic, "127.0.0.1", "MetaStreaming", results);
    consumer.start();

    StreamListener listener = new StreamListener(results);
    listener.start();
  }
}
