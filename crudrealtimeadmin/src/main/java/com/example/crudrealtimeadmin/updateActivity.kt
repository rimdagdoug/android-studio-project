package com.example.crudrealtimeadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.crudrealtimeadmin.databinding.ActivityUpdateBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class updateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val vehicleNumber = intent.getStringExtra("vehicleNumber")

        if (vehicleNumber != null) {
            loadDataFromFirebase(vehicleNumber)
        } else {
            Toast.makeText(this, "Invalid vehicle number", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.updateButton.setOnClickListener {
            val referenceVehicleNumber = binding.referenceVehicleNumber.text.toString()
            val updateOwnerName = binding.updateOwnerName.text.toString()
            val updateVehicleBrand = binding.updateVehicleBrand.text.toString()
            val updateVehicleRTO = binding.updateVehicleRto.text.toString()
            updateData(referenceVehicleNumber, updateOwnerName, updateVehicleBrand, updateVehicleRTO)
        }

        binding.retourButton.setOnClickListener {
            val intent = Intent(this@updateActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadDataFromFirebase(vehicleNumber: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Vehicle Information")
        //lire les données de l'emplacement de la base de données
        databaseReference.child(vehicleNumber).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // extrait les données de la base de données Firebase à partir du DataSnapshot
                    val vehicleData = dataSnapshot.value as Map<*, *>

                    binding.referenceVehicleNumber.setText(vehicleData["vehicleNumber"].toString())
                    binding.updateOwnerName.setText(vehicleData["ownerName"].toString())
                    binding.updateVehicleBrand.setText(vehicleData["vehicleBrand"].toString())
                    binding.updateVehicleRto.setText(vehicleData["vehicleRTO"].toString())
                } else {
                    Toast.makeText(this@updateActivity, "Vehicle data not found", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@updateActivity, "erreurs lors de la récupération des données", Toast.LENGTH_SHORT).show()

            }
        })
    }

    private fun updateData(vehicleNumber: String, ownerName: String, vehicleBrand: String, vehicleRTO: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Vehicle Information")
        // Crée une map (HashMap) contenant les nouvelles données du véhicule
        val vehicleData = mapOf(
            "vehicleNumber" to vehicleNumber,
            "ownerName" to ownerName,
            "vehicleBrand" to vehicleBrand,
            "vehicleRTO" to vehicleRTO
        )

        databaseReference.child(vehicleNumber).updateChildren(vehicleData).addOnSuccessListener {
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()

            val intent = Intent(this@updateActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Unable to update", Toast.LENGTH_SHORT).show()
        }
    }
}