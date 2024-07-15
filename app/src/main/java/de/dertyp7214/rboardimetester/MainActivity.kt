package de.dertyp7214.rboardimetester

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import de.dertyp7214.rboardcomponents.components.CheckCard
import de.dertyp7214.rboardcomponents.components.SearchBar
import de.dertyp7214.rboardcomponents.core.setHeight
import de.dertyp7214.rboardcomponents.core.setMargin
import de.dertyp7214.rboardcomponents.core.setWidth
import de.dertyp7214.rboardimetester.adapter.CheckCardAdapter
import de.dertyp7214.rboardimetester.core.capitalize
import de.dertyp7214.rboardimetester.core.getDrawableIdByName
import de.dertyp7214.rboardimetester.core.getStringIdByName
import de.dertyp7214.rboardimetester.core.openUrl
import de.dertyp7214.rboardimetester.data.CheckCardData
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private val checkCardIme = arrayListOf<CheckCardData>()
    private val checkCardsTypes = arrayListOf<CheckCardData>()

    private val cardView by lazy { findViewById<MaterialCardView>(R.id.cardView) }
    private val searchBar by lazy { findViewById<SearchBar>(R.id.searchBar) }
    private val recyclerViewIme by lazy { findViewById<RecyclerView>(R.id.recyclerViewIme) }
    private val recyclerViewTypes by lazy { findViewById<RecyclerView>(R.id.recyclerViewTypes) }

    private val otherCardsWrapper by lazy { findViewById<LinearLayout>(R.id.otherCards) }

    private val adapterIme by lazy {
        CheckCardAdapter(this, checkCardIme)
    }

    private val adapterTypes by lazy {
        CheckCardAdapter(this, checkCardsTypes)
    }

    private val defaultImeOption = EditorInfo.IME_ACTION_DONE

    private val imeOptions = listOf(
        EditorInfo.IME_ACTION_NONE to "none",
        EditorInfo.IME_ACTION_DONE to "done",
        EditorInfo.IME_ACTION_GO to "go",
        EditorInfo.IME_ACTION_NEXT to "next",
        EditorInfo.IME_ACTION_PREVIOUS to "previous",
        EditorInfo.IME_ACTION_SEARCH to "search",
        EditorInfo.IME_ACTION_SEND to "send"
    )

    private val defaultInputType = EditorInfo.TYPE_CLASS_TEXT

    private val inputTypes = listOf(
        EditorInfo.TYPE_CLASS_DATETIME to "datetime",
        EditorInfo.TYPE_CLASS_NUMBER to "number",
        EditorInfo.TYPE_CLASS_PHONE to "phone",
        EditorInfo.TYPE_CLASS_TEXT to "text",
    )

    private val otherCards = arrayListOf<Pair<CheckCard, Boolean>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchBar.imeReturn = false

        cardView.setMargin(
            bottomMargin = -resources.getDimension(de.dertyp7214.rboardcomponents.R.dimen.roundCornersInner)
                .roundToInt()
        )

        otherCards.addAll(listOf(
            CheckCard(this) {
                text = R.string.incognito
                icon = R.drawable.ic_incognito
                visibility =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) View.VISIBLE else View.GONE

                setOnClickListener {
                    optionOnClick(null)
                    if (it) adapterIme.uncheckAll(null)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) searchBar.incognito = it
                }
            } to true,
            CheckCard(this) {
                text = R.string.password
                icon = R.drawable.ic_password

                setOnClickListener {
                    optionOnClick(null)
                    uncheckCards(this, it)

                    searchBar.password = it
                }
            } to false,
            CheckCard(this) {
                text = R.string.password_number
                icon = R.drawable.ic_password

                setOnClickListener {
                    optionOnClick(null)
                    uncheckCards(this, it)

                    searchBar.passwordNumber = it
                }
            } to false,
            CheckCard(this) {
                text = R.string.email
                icon = R.drawable.ic_email

                setOnClickListener {
                    optionOnClick(null)
                    uncheckCards(this, it)

                    searchBar.email = it
                }
            } to false
        ))

        checkCardIme.addAll(imeOptions.map(::parseImeOption))
        checkCardsTypes.addAll(inputTypes.map(::parseInputType))

        var currentLayout: LinearLayout? = null
        otherCards.forEachIndexed { index, (checkCard, _) ->
            if (index % 3 == 0) {
                currentLayout = LinearLayout(this).apply {
                    setWidth(LayoutParams.MATCH_PARENT)
                    setHeight(LayoutParams.WRAP_CONTENT)
                    orientation = HORIZONTAL
                    otherCardsWrapper.addView(this)
                }
            }
            currentLayout?.addView(checkCard)
        }

        listOf(recyclerViewIme, recyclerViewTypes).forEach { recyclerView ->
            recyclerView.layoutManager = GridLayoutManager(this, 3)
            recyclerView.setHasFixedSize(true)
        }
        recyclerViewIme.adapter = adapterIme
        recyclerViewTypes.adapter = adapterTypes

        adapterIme.notifyItemRangeChanged(0, imeOptions.size)
        adapterTypes.notifyItemRangeChanged(0, inputTypes.size)

        searchBar.setMenu(R.menu.menu) {
            when (it.itemId) {
                R.id.menu_github -> {
                    openUrl(getString(R.string.github_url))
                    true
                }
                R.id.menu_telegram -> {
                    openUrl(getString(R.string.telegram_channel_url))
                    true
                }
                else -> false
            }
        }
    }

    private fun optionOnClick(int: Int?, ime: Boolean = true) {
        if (int == null) {
            searchBar.close()
            searchBar.focus()
        } else {
            if (ime) {
                otherCards.forEach { it.first.isChecked = false }
                searchBar.imeOptions = int
            } else searchBar.inputType = int
            searchBar.close()
            searchBar.focus()
        }
    }

    private fun uncheckCards(except: CheckCard?, checked: Boolean) {
        otherCards.filter { it.first != except && !it.second }.forEach {
            it.first.isChecked = false
        }
        if (checked) listOf(adapterIme).forEach { adapter ->
            adapter.uncheckAll(null)
        }
    }

    private fun parseImeOption(option: Pair<Int, String>): CheckCardData {
        return CheckCardData(
            text = getStringIdByName("ime_option_${option.second}"),
            icon = getDrawableIdByName("ime_option_${option.second}"),
            isChecked = option.first == defaultImeOption
        ) {
            searchBar.hint = option.second.capitalize()
            optionOnClick(option.first, true)
        }
    }

    private fun parseInputType(option: Pair<Int, String>): CheckCardData {
        return CheckCardData(
            text = getStringIdByName("input_type_${option.second}"),
            icon = getDrawableIdByName("input_type_${option.second}"),
            isChecked = option.first == defaultInputType
        ) {
            optionOnClick(option.first, false)
        }
    }
}