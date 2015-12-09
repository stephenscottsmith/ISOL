#!/usr/bin/env swift

import Darwin
import Foundation

if Process.arguments.count != 2 {
    fputs("Exactly one argument is required\n", __stderrp)
    exit(1)
}

var titles = [String]()

func shell(args: String...) -> Int32 {
    let task = NSTask()
    task.launchPath = "/usr/bin/env"
    task.arguments = args

    let pipe = NSPipe()
    task.standardOutput = pipe



    task.launch()
    let data = pipe.fileHandleForReading.readDataToEndOfFile()
    let output: String = NSString(data: data, encoding: NSUTF8StringEncoding)! as String
    titles = output.componentsSeparatedByString("\n")
    task.waitUntilExit()
    return task.terminationStatus
}



shell("ls", "/Volumes/Drobo/Library")
titles.removeAtIndex((titles.count - 1))

for var title in titles {
	print(title)
}

print(titles.count)

print("hello")

// let word = Process.arguments[1]
// print(Process.arguments[0])

// print(Process.arguments[1])
var s = "hello.iso.iso"
let r : Range<String.Index> = Range<String.Index>(start: 0, end: 9)
var x = s.substringWithRange(Range<Index>(start: 0, end: 9))
print(r.startIndex)
print(x)