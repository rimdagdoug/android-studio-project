package com.example.crudrealtimeadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.crudrealtimeadmin.databinding.ActivityMainBinding
import com.example.crudrealtimeadmin.databinding.ActivityUploadBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var databaseReference:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.retourButton.setOnClickListener {
            val intent = Intent(this@UploadActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.saveButton.setOnClickListener {
            val ownerName = binding.uploadOwnerName.text.toString()
            val vehicleBrand = binding.uploadVehicaleBrand.text.toString()
            val vehicleRTO = binding.uploadVehicaleRTO.text.toString()
            val vehicleNumber = binding.uploadVehicaleNumber.text.toString()
            //référence à la base de données Firebase Realtime
            databaseReference= FirebaseDatabase.getInstance().getReference("Vehicle Information")
           //instance de la classe VehicleData
            val vehicleData=VehicleData(ownerName,vehicleBrand,vehicleRTO,vehicleNumber)
            //Enregistrement des données:vehicleNumber est utilisé comme clé unique pour identifier le véhicule
            databaseReference.child(vehicleNumber).setValue(vehicleData).addOnSuccessListener {
                binding.uploadOwnerName.text.clear()
                binding.uploadVehicaleBrand.text.clear()
                binding.uploadVehicaleRTO.text.clear()
                binding.uploadVehicaleNumber.text.clear()

                Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show()
                val intent=Intent(this@UploadActivity,MainActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
            }
        }
    }
}