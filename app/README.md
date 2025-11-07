**Billetera Digital — Frontend Android (Jetpack Compose)

**Requisitos:::::::::**

Android Studio  | 2025.2.1
Build #AI-252.25557.131.2521.14344949, built on October 28, 2025
Runtime version: 21.0.8+-14196175-b1038.72 amd64
VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.

JDK 17
minSdk 24, targetSdk 35
Kotlin 1.9.25
Compose BOM 2024.12.x, Material3

Devices: andorid 15.0 vanillaIceCream

**Estructura del proyecto**

app/
└── src/main/java/com/example/billetera_digital/
├── ui/
│   ├── components/
│   │   ├── Buttons.kt
│   │   ├── Fields.kt
│   │   ├── Keypad.kt
│   │   ├── MovementRow.kt
│   │   ├── OutlinedButton.kt
│   │   ├── PrimaryButton.kt
│   │   ├── SixDigitPinField.kt
│   │   ├── TopBars.kt
│   │   └── WalletCard.kt
│   ├── screens/
│   │   ├── SplashScreen.kt
│   │   ├── RegisterScreen.kt
│   │   ├── OtpScreen.kt
│   │   ├── LoginScreen.kt
│   │   ├── HomeScreen.kt
│   │   ├── SearchContactScreen.kt
│   │   ├── AmountScreen.kt
│   │   ├── ConfirmPinScreen.kt
│   │   ├── SendConfirmScreen.kt
│   │   ├── SuccessScreen.kt
│   │   ├── ReceiveScreen.kt
│   │   ├── HistoryScreen.kt
│   │   ├── ProfileScreens.kt
│   │   └── ProfileDataScreen.kt
│   └── theme/
│       ├── BilleteraTheme.kt
│       ├── Theme.kt
│       ├── Color.kt
│       ├── Dimens.kt
│       └── Type.kt
├── state/
│   ├── UserViewModel.kt
│   └── SendViewModel.kt
├── AppNav.kt
└── MainActivity.kt


**Rol de cada archivo clave**

MainActivity.kt: punto de entrada. Monta BilleteraTheme y AppNav(). "el app nav esta configurado en el manifest para q se ejecute cmo si guera un main"

AppNav.kt: NavHost con todas las rutas. Define navegación entre pantallas y callbacks.

state/UserViewModel.kt: estado de usuario. Campos: name, alias, phone, email, balance, showBalance. Acciones: toggleShowBalance(), logout(), loadProfile(), loadBalance().

state/SendViewModel.kt: estado del flujo de envío. Campos: contact, amount (string “S/ 123”), note, pin. Acciones: press(char), backspace(), amountDouble(), pick(contact), setPin(s), clear().

components/Buttons.kt: botones primarios, tonales y outline estandarizados.

components/Fields.kt: campos de formulario con estilos consistentes (label, error, supportingText).

components/Keypad.kt: teclado numérico reutilizable para monto.

components/SixDigitPinField.kt: campo PIN con PasswordVisualTransformation, validación 6 dígitos.

components/TopBars.kt: TopAppBar y variantes con botón atrás y título.

components/WalletCard.kt: tarjeta de saldo con opción mostrar/ocultar usando UserViewModel.showBalance.

screens/: cada archivo es una pantalla independiente. Sin lógica de red. Solo UI + llamadas a ViewModels.

theme/: colores, tipografías, dimensiones, formas. Todo el branding.

__________________________________________________________________________________

**Flujo de usuario y rutas**

**Onboarding**

SplashScreen → botones “Crear cuenta” y “Iniciar sesión”.

Registro: RegisterScreen → valida campos locales → OtpScreen para código 6 dígitos → a HomeScreen.

Login: LoginScreen → email + password/PIN → a HomeScreen.

**Home**

HomeScreen muestra WalletCard con saldo y toggle “ojito”. Acciones: “Yapear”, “Recibir”, “Historial”, “Perfil”.

**Enviar dinero**

SearchContactScreen filtra lista por alias/wallet → SendViewModel.pick().

AmountScreen usa Keypad y valida amountDouble()>0.

SendConfirmScreen muestra resumen.

ConfirmPinScreen usa SixDigitPinField → si OK → SuccessScreen y limpia estado.

**Recibir dinero**

ReceiveScreen muestra alias y wallet. Copia al portapapeles y lanza Snackbar.

**Perfil**

ProfileScreens.kt agrupa tarjetas de opciones. ProfileDataScreen muestra datos leídos de UserViewModel.

__________________________________________________________

**Interacción entre capas**

Pantalla → emite eventos de UI → ViewModel actualiza estado observable → Compose recompondrá.

Navegación se ejecuta desde callbacks en AppNav.kt y se inyectan a cada pantalla como lambdas.

Backend (a integrar): los ViewModels llaman a un Repository (a crear) con suspend functions. No mezclar llamadas de red en pantallas.

__________________________________________________________

**Sugerencia de Puntos de integración con el backend**

Crea una interfaz Repository y su implementación con Retrofit/OkHttp. Inyéctala en los ViewModel (constructor o Service Locator simple).

interface Repository {
suspend fun register(name: String, alias: String, phone: String, email: String): Result<Unit>
suspend fun verifyOtp(email: String, code: String): Result<Unit>
suspend fun login(email: String, pass: String): Result<TokenDto>
suspend fun getProfile(): Result<ProfileDto>
suspend fun getBalance(): Result<BalanceDto>
suspend fun searchContacts(q: String): Result<List<ContactDto>>
suspend fun sendTransfer(toWallet: String, amount: Long, note: String?, pin: String): Result<TxDto>
suspend fun getHistory(): Result<List<TxDto>>
}


Convertir monto: UI usa string “S/ 32”. Enviar entero en céntimos (amountCentavos: Long).

Errores: propagar como Result.Failure con mensaje. UI muestra supportingText o Snackbar.

Autenticación: mantener token en DataStore. Interceptor agrega Authorization: Bearer <token>.

__________________________________________________________

**Sugerencia de endpoints**

POST /auth/register

POST /auth/verify-otp

POST /auth/login

GET /me → perfil

GET /wallet/balance

GET /contacts?q=

POST /transfers {toWallet, amount, note, pin}

GET /transfers/history

**Dónde llamar**

RegisterScreen → Repository.register

OtpScreen → Repository.verifyOtp

LoginScreen → Repository.login

HomeScreen.onResume → Repository.getBalance

SearchContactScreen onTextChange debounce → Repository.searchContacts

ConfirmPinScreen onConfirm → Repository.sendTransfer

HistoryScreen onOpen → Repository.getHistory

ProfileScreens/ProfileDataScreen → Repository.getProfile

__________________________________________________________

**Estados y validaciones en UI**

RegisterScreen

alias.startsWith("@") && alias.length >= 3 (alias es el nombre de usuario q se le puede asignar si no escribe el numero de cel)

phone.length in 9..12 && phone.all(Char::isDigit) (contabiliza que sean 9 digitos el celular)

email.contains("@") && email.contains(".") (asgura que el correo tenga arroba y punto)

OtpScreen: PIN de 6 dígitos. Error “Debe tener 6 dígitos”. (controla que el pin tenga como minimo 6 digitos osea la contraseña es numerica)

AmountScreen: amountDouble() > 0. Mensaje: “Ingresa un monto mayor a 0”. (controla la cantidad minima transferencia, esta por defecto mayor a 0)

ConfirmPinScreen: SixDigitPinField bloquea caracteres no numéricos.

SearchContactScreen: Placeholder y mensaje “Escribe para buscar”. Manejar “Sin resultados”. 

__________________________________________________________

**Navegación y rutas**

**Rutas**: "splash", "register", "otp", "login", "home", "search", "amount", "sendConfirm", "confirmPin", "success", "receive", "history", "profile", "profileData".

**Reglas**: config de pantallas puras; navigation args mínimos; estado en ViewModels.

**Tema y branding**

theme/Color.kt: define la paleta del diseño . Por ejemplo:

val Brand = Color(0xFF5A67F2)        // primario
val BrandVariant = Color(0xFF7C8BFF) // gradiente
val BackgroundForm = Color(0xFFF6F4FF)


theme/Theme.kt o BilleteraTheme.kt: configura lightColorScheme() con primary, onPrimary, surface, onSurface, outline, etc.

Dimens.kt: radios, paddings.

Type.kt: pesos y tamaños de tipografía.

**Colores de botones y cards**

Botón primario toma MaterialTheme.colorScheme.primary.

PrimaryButton y OutlinedButton en components/Buttons.kt. Ajusta ahí para reflectar el branding.

WalletCard define gradiente y formato de saldo.

__________________________________________________________

**Ocultar/mostrar saldo**

UserViewModel.showBalance: Boolean con toggleShowBalance().

WalletCard.kt usa un IconButton con Icons.Outlined.Visibility/VisibilityOff. Si showBalance=false, muestra “•••••” en lugar del monto.

Persistencia opcional en DataStore.

__________________________________________________________


**Pantalla de login y assets**
por si quieres cambiar el logo: 

la direccion del logo en app/src/main/res/drawable/logo_wallet.png.
En LoginScreen.kt o SplashScreen.kt se modifica:

Image(
painter = painterResource(R.drawable.mi_logo),
contentDescription = null,
modifier = Modifier
.size(96.dp)
.clip(RoundedCornerShape(20.dp))
.background(MaterialTheme.colorScheme.surface)
.padding(12.dp)
)


Fondo con gradiente:

Box(
Modifier
.fillMaxSize()
.background(
Brush.verticalGradient(
listOf(Brand, BrandVariant)
)
)
) { /* contenido */ }

__________________________________________________________

Cómo estilizar formularios

Fondo uniforme de pantallas de formulario: en cada Scaffold usa containerColor = BackgroundForm.

Campos: usa FormTextField de Fields.kt. Propaga isError y supportingText.
__________________________________________________________

Convenciones

UI pura: sin Retrofit ni lógica de sesión.

ViewModels: orquestación + validaciones + llamadas a repositorio.

Result para errores. Mensajes cortos y claros.

Centavos para montos.

__________________________________________________________

**Checklist de integración****SUGERENCIA**

Implementar Repository y DI simple.

Conectar register/login/otp.

Cargar balance en HomeScreen.

Buscar contactos con debounce en SearchContactScreen.

Enviar transferencia desde ConfirmPinScreen.

Mostrar historial.

Manejo de errores y snackbars.

Persistir token y showBalance.

__________________________________________________________

Build

Min SDK 24. Target 35.

Material 3 + Compose BOM.

AGP 8.6+ y Kotlin conforme al mapa Compose/Kotlin.

JVM target 17.

__________________________________________________________

Para hacer pruebas:

Registro con datos inválidos.

OTP < 6 dígitos.

Monto 0.

PIN incorrecto.

Contacto inexistente.

Sin red en confirmación de transferencia.

Alternar “ocultar saldo” y persistencia.