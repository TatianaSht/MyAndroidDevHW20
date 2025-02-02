package com.example.myandroiddevhw20.presentation.fragments

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myandroiddevhw20.App
import com.example.myandroiddevhw20.R
import com.example.myandroiddevhw20.data.model.Photo
import com.example.myandroiddevhw20.databinding.FragmentListPhotoBinding
import com.example.myandroiddevhw20.presentation.ViewModelFactory
import com.example.myandroiddevhw20.presentation.viewmodels.ListPhotoAdapter
import com.example.myandroiddevhw20.presentation.viewmodels.ListPhotoViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.messaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class ListPhotoFragment : Fragment() {
    private var _binding: FragmentListPhotoBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var listPhotoViewModelFactory: ViewModelFactory
    private val viewModel: ListPhotoViewModel by viewModels { listPhotoViewModelFactory }

    private val listPhotoAdapter = ListPhotoAdapter(
        onClick = { photo -> onClickPhoto(photo) }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListPhotoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!NotificationManagerCompat.from(requireContext()).areNotificationsEnabled()) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Notifications are disabled")
                .setMessage("Notifications are disabled for this application. Enable?")
                .setPositiveButton("Yes") { _, _ ->
                    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                        putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                    }
                    startActivity(intent)
                }
                .setNegativeButton("No", null)
                .show()
        }

        Firebase.messaging.isAutoInitEnabled = true
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG_TO_LOG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            Log.d(TAG_TO_LOG, token)
        })


        binding.buttonTakeNewPhoto.setOnClickListener {
            findNavController().navigate(R.id.action_ListPhotoFragment_to_TakePhotoFragment)

            FirebaseCrashlytics.getInstance().log("This is log message with additional info")
            try {
                throw Exception("My first exception")
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
            }
            createNotification()
        }

        binding.buttonGoToMap.setOnClickListener {
            findNavController().navigate(R.id.action_ListPhotoFragment_to_MapFragment)
        }

        binding.recyclerView.adapter = listPhotoAdapter

        viewModel.listPhotos.onEach {
            listPhotoAdapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun onClickPhoto(item: Photo) {
        MaterialAlertDialogBuilder(requireContext())
            .setBackground(Drawable.createFromPath(item.uri))
            .setMessage("Date: ${item.date}")
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("MissingPermission")
    private fun createNotification() {
        val intent = Intent(requireContext(), ListPhotoFragment::class.java)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            PendingIntent.getActivity(
                requireContext(),
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        else
            PendingIntent.getActivity(
                requireContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        val notification = NotificationCompat.Builder(requireContext(), App.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("My first notification")
            .setContentText("Description of my first notification")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(requireContext()).notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val NOTIFICATION_ID = 1000
        private const val TAG_TO_LOG = "registration token"
    }
}