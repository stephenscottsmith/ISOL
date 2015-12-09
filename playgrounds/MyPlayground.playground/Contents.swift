#!/usr/bin/env swift

import Darwin
import Foundation

//if Process.arguments.count != 2 {
//    fputs("Exactly one argument is required\n", __stderrp)
//    exit(1)
//}
//
//var titles = [String]()
//
//func shell(args: String...) -> Int32 {
//    let task = NSTask()
//    let pipe = NSPipe()
//    task.standardOutput = pipe
//    task.launchPath = "/usr/bin/env"
//    task.arguments = args
//    task.launch()
//    task.waitUntilExit()
//    let data = pipe.fileHandleForReading.readDataToEndOfFile()
//    let output : String = NSString(data: data, encoding: NSASCIIStringEncoding)! as String
//    print(output)
//    print("DONE")
//    return task.terminationStatus
//}

//shell("cd /Volumes/Drobo/Library; ls")

var s = "hello.iso"
let r : Range<String.Index> = s.rangeOfString(".iso", options:NSStringCompareOptions.BackwardsSearch)!
var x = s.substringWithRange(Range<String.Index>(start: s.startIndex, end: r.endIndex))
print(r.startIndex)
print(x)