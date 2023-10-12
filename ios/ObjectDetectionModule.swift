//
//  ObjectDetecionHelper.swift
//  ObjDetection
//
//  Created by Chaitanyaa Adaki on 02/10/23.
//

import Foundation
import React
import MLKit

@objc(ObjectDetectionModule)
class ObjectDetectionModule: NSObject {

  @objc static func requiresMainQueueSetup() -> Bool {
    return true
  }

  let photoObjectDetector: ObjectDetector

  override init() {
    let options = ObjectDetectorOptions()
    options.detectorMode = .singleImage
    options.shouldEnableMultipleObjects = true
    options.shouldEnableClassification = true
    photoObjectDetector = ObjectDetector.objectDetector(options: options)
  }

  @objc
  func startObjectDetection(_ image: String, resolver: @escaping RCTPromiseResolveBlock, rejecter: @escaping RCTPromiseRejectBlock) {
    
    if let imageUrl = URL(string: image),
        let imageData = try? Data(contentsOf: imageUrl),
        let img = UIImage(data: imageData) {
  
      let visionImage = VisionImage(image: img)
      visionImage.orientation = img.imageOrientation
   
      photoObjectDetector.process(visionImage) { objects, error in
        guard error == nil else {
          print(error ?? "error")
          return
        }
        
        if let objects = objects, !objects.isEmpty {
          for object in objects {
            let frame = object.frame
            let trackingID = object.trackingID
            print(frame,trackingID ?? 0)
            
            // If classification was enabled:
            let description = object.labels.enumerated().map { (index, label) in
              "Label \(index): \(label.text), \(label.confidence)"
            }.joined(separator: "\n")
            resolver(description)
            print(description,"description")
          }
        } else {
          let code = "400"
          let message = "No Object Detected"
          let error = NSError(domain: "YourDomain", code: 400, userInfo: nil)
          rejecter(code,message,error)
          print("No object Detected")
        }
      }
    }
    else{
      print("No object Detected")
    }
  }
}

