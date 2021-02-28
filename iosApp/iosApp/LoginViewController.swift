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
        } catch let error as NSError {
            sender.isEnabled = true
            error.userInfo
            print(error)
        } catch{
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
