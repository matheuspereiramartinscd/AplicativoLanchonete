package com.example.controledevendasestoque

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.controledevendasestoque.ui.theme.ControleDeVendasEstoqueTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ControleDeVendasEstoqueTheme {
                ControleDeVendasEstoqueApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControleDeVendasEstoqueApp() {
    var produto by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }
    var produtos by remember { mutableStateOf(mutableMapOf<String, Int>()) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Controle de Vendas e Estoque") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF6200EE),
                    titleContentColor = Color.White
                )
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = produto,
                    onValueChange = { produto = it },
                    label = { Text("Nome do Produto") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF6200EE),
                        cursorColor = Color(0xFF6200EE)
                    )
                )

                OutlinedTextField(
                    value = quantidade,
                    onValueChange = { quantidade = it },
                    label = { Text("Quantidade") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF6200EE),
                        cursorColor = Color(0xFF6200EE)
                    )
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            val qtd = quantidade.toIntOrNull()
                            if (produto.isNotEmpty() && qtd != null) {
                                produtos[produto] = (produtos[produto] ?: 0) + qtd
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Produto cadastrado com sucesso")
                                }
                            } else {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Erro: Dados inválidos")
                                }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cadastrar Produto")
                    }

                    Button(
                        onClick = {
                            val qtd = quantidade.toIntOrNull()
                            if (produto.isNotEmpty() && qtd != null && produtos[produto] != null && produtos[produto]!! >= qtd) {
                                produtos[produto] = produtos[produto]!! - qtd
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Produto vendido")
                                }
                            } else {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Erro: Estoque insuficiente")
                                }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Vender Produto")
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    items(produtos.entries.toList()) { entry ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "${entry.key}: ${entry.value} unidades")
                            Button(onClick = { produtos[entry.key] = 0 }) {
                                Text(text = "Resetar Estoque")
                            }
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ControleDeVendasEstoqueAppPreview() {
    ControleDeVendasEstoqueTheme {
        ControleDeVendasEstoqueApp()
    }
}
