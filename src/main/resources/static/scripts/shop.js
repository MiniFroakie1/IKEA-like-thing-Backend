function load(rebate) {
    localStorage.setItem("rebate", rebate)
    selectUser(document.getElementById('user-value').value)
    fillCartTable()
}

function selectUser(user) {
    if (user === "") {
        return
    }
    let userSelection = document.getElementById("user").children.namedItem(user)
    userSelection.selected = true
}

function createCart(products) {
    let cart = []
    for (let i = 0; i < products.length; i++) {
        cart[i] = ({name:products[i].name, price:products[i].price, count:0})
    }
    localStorage.setItem("cart", JSON.stringify(cart))
}

function addToCart(id) {
    id--
    const cart = JSON.parse(localStorage.getItem("cart"))
    cart[id].count++
    localStorage.setItem("cart", JSON.stringify(cart))
    fillCartTable()
}

function removeFromCart(id) {
    id--
    const cart = JSON.parse(localStorage.getItem("cart"))
    if (cart[id].count <= 0) {
        return
    }
    cart[id].count--
    localStorage.setItem("cart", JSON.stringify(cart))
    fillCartTable()
}

function setItemCount(id, count) {
    count = +count
    if (count < 0) {
        count = 0
    }
    id--
    const cart = JSON.parse(localStorage.getItem("cart"))
    cart[id].count = count
    localStorage.setItem("cart", JSON.stringify(cart))
    fillCartTable()
}

function fillCartTable() {
    const cart = JSON.parse(localStorage.getItem("cart"))
    const rebate =  localStorage.getItem("rebate")
    let totalPrice = 0;
    let totalCount = 0;
    for (let i = 0; i < cart.length; i++) {
        document.getElementById('product count ' + (i + 1)).value = cart[i].count
        document.getElementById('product price ' + (i + 1)).innerText = ((cart[i].price * cart[i].count) - (cart[i].price * cart[i].count) * (rebate / 100)).toLocaleString() + ' kr'
        totalPrice += cart[i].price * cart[i].count
        totalCount += cart[i].count
    }
    document.getElementById("total-count").innerText = totalCount;
    document.getElementById("total-price").innerText = (totalPrice - totalPrice * (rebate / 100)).toLocaleString() + ' kr';

}