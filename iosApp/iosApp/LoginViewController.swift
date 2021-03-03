//
//  LoginViewController.swift
//  iosApp
//
//  Created by Eugene Levenetc on 28/02/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import UIKit
import shared

class LoginViewController: UIViewController {

    @IBOutlet weak var textFieldPassword: UITextField!
    @IBOutlet weak var buttonLogin: UIButton!
    @IBOutlet weak var labelStatus: UILabel!
    
    let dao = Dao(dbDriverFactory: DatabaseDriverFactory())
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let flow = dao.flowCall()
        let collector = Collector<String>(callback: {_ in
            print(">>>")
        })
        
//        flow.collect(collector: collector, completionHandler: { (s1: KotlinUnit?, s2: Error) -> Void in
//            print("zed")
//        })
        
        flow.collect(collector: collector) { (result: KotlinUnit?, error: Error?) in
            print("completion")
        }
        
        if(dao.isLocked()){
            labelStatus.text = "locked"
        }else{
            labelStatus.text = "unlocked"
        }
        
    }
    
    @IBAction func handleLogin(_ sender: UIButton) {
        let password = textFieldPassword.text!
        
        do {
            try dao.unlock(key: password)
            labelStatus.text = "Unlocked"
            sender.isEnabled = false
        } catch let error as InvalidDbPassword {
            labelStatus.text = "Invalid password"
            sender.isEnabled = true
            print(error)
        } catch let error as KotlinException {
            sender.isEnabled = true
            print(error)
        } catch let error as NSError{
            
            if(error.isShared(InvalidDbPassword.self) && 1 == 1){
                
            }
            
            sender.isEnabled = true
            print(error)
        } catch {
            sender.isEnabled = true
            print("enknown error")
            print(error)
        }
        
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}

extension NSError {
    func isShared(_ exception:AnyClass)-> Bool{
        //exception.class().na
        return true
    }
}

class Collector<T>: Kotlinx_coroutines_coreFlowCollector {
    let callback: (T) -> Void
    init(callback: @escaping (T) -> Void) {
        self.callback = callback
    }

    func emit(value: Any?, completionHandler: @escaping (KotlinUnit?, Error?) -> Void) {
        callback(value as! T)
        completionHandler(KotlinUnit(), nil)
    }
}
