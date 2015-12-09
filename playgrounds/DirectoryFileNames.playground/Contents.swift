import Cocoa

class File {
    class func open (path: String, utf8: NSStringEncoding = NSUTF8StringEncoding) -> String? {
        try {
           return NSFileManager().fileExistsAtPath(path) ? String(contentsOfFile: path, encoding: utf8) : ""
        } catch {
            
        }
    }
    class func save (path: String, _ content: String, utf8: NSStringEncoding = NSUTF8StringEncoding) -> Bool {
        return try content.writeToFile(path, atomically: true, encoding: utf8)
    }
}

let stringToSave:String = "Your text"

let didSave = File.save("\(NSHomeDirectory())/Desktop/file.txt", stringToSave)

if didSave { print("file saved") } else { print("error saving file") }