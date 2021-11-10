package com.d4d5.myfamily

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.widget.ImageView
import com.d4d5.myfamily.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {

    lateinit var inviteAdapter: InviteAdapter
    lateinit var mContext: Context
    private val listContacts: ArrayList<ContactModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("FetchContact89", "onViewCreated: ")

        val listMembers = listOf<MemberModel>(
            MemberModel(
                "Lokesh",
                "9th buildind, 2nd floor, maldiv road, manali 9th buildind, 2nd floor",
                "90%",
                "220"
            ),
            MemberModel(
                "Kedia",
                "10th buildind, 3rd floor, maldiv road, manali 10th buildind, 3rd floor",
                "80%",
                "210"
            ),
            MemberModel(
                "D4D5",
                "11th buildind, 4th floor, maldiv road, manali 11th buildind, 4th floor",
                "70%",
                "200"
            ),
            MemberModel(
                "Ramesh",
                "12th buildind, 5th floor, maldiv road, manali 12th buildind, 5th floor",
                "60%",
                "190"
            ),
        )

        val adapter = MemberAdapter(listMembers)

        binding.recyclerMember.layoutManager = LinearLayoutManager(mContext)
        binding.recyclerMember.adapter = adapter



        Log.d("FetchContact89", "fetchContacts: start karne wale hai")

        Log.d("FetchContact89", "fetchContacts: start hogya hai ${listContacts.size}")
        inviteAdapter = InviteAdapter(listContacts)
        fetchDatabaseContacts()
        Log.d("FetchContact89", "fetchContacts: end hogya hai")

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("FetchContact89", "fetchContacts: coroutine start")

            insertDatabaseContacts(fetchContacts())

            Log.d("FetchContact89", "fetchContacts: coroutine end ${listContacts.size}")
        }



        binding.recyclerInvite.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerInvite.adapter = inviteAdapter



        binding.iconThreeDots.setOnClickListener {

            SharedPref.putBoolean(PrefConstants.IS_USER_LOGGED_IN, false)

            FirebaseAuth.getInstance().signOut()

        }

    }

    private fun fetchDatabaseContacts() {
        val database = MyFamilyDatabase.getDatabase(mContext)

        database.contactDao().getAllContacts().observe(viewLifecycleOwner) {

            Log.d("FetchContact89", "fetchDatabaseContacts: ")

            listContacts.clear()
            listContacts.addAll(it)

            inviteAdapter.notifyDataSetChanged()

        }
    }

    private suspend fun insertDatabaseContacts(listContacts: ArrayList<ContactModel>) {

        val database = MyFamilyDatabase.getDatabase(mContext)

        database.contactDao().insertAll(listContacts)

    }


    private fun fetchContacts(): ArrayList<ContactModel> {

        Log.d("FetchContact89", "fetchContacts: start")
        val cr = mContext.contentResolver
        val cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        val listContacts: ArrayList<ContactModel> = ArrayList()


        if (cursor != null && cursor.count > 0) {

            while (cursor != null && cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhoneNumber =
                    cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                if (hasPhoneNumber > 0) {

                    val pCur = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        ""
                    )

                    if (pCur != null && pCur.count > 0) {

                        while (pCur != null && pCur.moveToNext()) {

                            val phoneNum =
                                pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                            listContacts.add(ContactModel(name, phoneNum))

                        }
                        pCur.close()

                    }

                }
            }

            if (cursor != null) {
                cursor.close()
            }

        }
        Log.d("FetchContact89", "fetchContacts: end")
        return listContacts

    }


    companion object {

        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}