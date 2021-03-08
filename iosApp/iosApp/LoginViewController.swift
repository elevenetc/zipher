//
//  LoginViewController.swift
//  iosApp
//
//  Created by Eugene Levenetc on 28/02/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import UIKit
import shared

class LoginViewController: UIViewController, UITextFieldDelegate {

    @IBOutlet weak var textFieldPassword: UITextField!
    @IBOutlet weak var labelStatus: UILabel!

    override func viewDidLoad() {
        super.viewDidLoad()
    
        textFieldPassword.becomeFirstResponder()
        
        let collector = Collector<ViewModel.StateTransition>(callback: {transition in
            let currentState = transition.currentState
            let prevState = transition.prevState
            self.labelStatus.text = String(describing: transition)
            
            print("state update: \(currentState)")
            
            if(currentState is LockViewModel.Unlocked || currentState is LockViewModel.LockCreated){
                self.textFieldPassword.resignFirstResponder()
                self.performSegue(withIdentifier: "unlocked", sender: self)
            }else if(currentState is LockViewModel.CreatingLockVerify && prevState is LockViewModel.CreatingLock){
                self.textFieldPassword.text = ""
            }
        })
        
        lockViewModel.state.collect(collector: collector) { (result: KotlinUnit?, error: Error?) in
            print("completion")
        }
        lockViewModel.onUserAction(action: LockViewModel.GetLockState())
        
        textFieldPassword.delegate = self
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        lockViewModel.onUserAction(action: LockViewModel.Next())
        return false
    }
    
    @IBAction func onPasswordChanged(_ sender: UITextField, forEvent event: UIEvent) {
        let key = sender.text!
        lockViewModel.onUserAction(action: LockViewModel.PassEntry(value: key))
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
