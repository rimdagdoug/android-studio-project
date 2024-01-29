package com.example.crudrealtimeadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.crudrealtimeadmin.databinding.ActivityCheckBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CheckActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckBinding
    //utilisée pour interagir avec la base de données Firebase en temps réel
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.retourButton.setOnClickListener {
            val intent = Intent(this@CheckActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.update.setOnClickListener {
            val verif = binding.checkVehicleNumber.text.toString()
            databaseReference = FirebaseDatabase.getInstance().getReference("Vehicle Information")

            // Vérifier si la valeur existe dans la base de données
            databaseReference.child(verif).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // La valeur existe dans la base de données
                        val intent = Intent(this@CheckActivity, updateActivity::class.java)
                        intent.putExtra("vehicleNumber", verif)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@CheckActivity, "n'existe pas ", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Gestion des erreurs lors de la récupération des données depuis la base de données
                    Toast.makeText(this@CheckActivity, "erreurs lors de la récupération des données", Toast.LENGTH_SHORT).show()

                }
            })
        }
    }
}
