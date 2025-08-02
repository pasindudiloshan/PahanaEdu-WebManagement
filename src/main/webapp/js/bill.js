$(document).ready(function () {
    $('.select2').select2();
    setDiscount();

    $('#productSelect').on('change', function () {
        const selected = $('#productSelect option:selected');
        const price = selected.attr('data-price');
        $('#unitPriceInput').val(price ? Number(price).toFixed(2) : '');
        clearTotals();
    });
});

function setDiscount() {
    const method = document.getElementById("paymentMethod").value;
    const discountMap = {
        "Cash": 3,
        "Credit Card": 15,
        "Debit Card": 7,
        "Bank Transfer": 0
    };
    document.getElementById("discountPercent").value = discountMap[method] || 0;
    recalculateGrandTotal();
}

function calculateSubtotal() {
    const quantity = parseInt($('#quantityInput').val());
    const unitPrice = parseFloat($('#unitPriceInput').val());
    const discountPercent = parseFloat($('#discountPercent').val());
    const selected = $('#productSelect option:selected');
    const stock = parseInt(selected.attr('data-stock') || "0");

    if (!selected.val()) {
        alert("Please select a product.");
        return;
    }

    if (isNaN(quantity) || quantity <= 0 || isNaN(unitPrice)) {
        alert("Please enter a valid quantity.");
        return;
    }

    if (stock === 0) {
        alert("Sold Out: This product is out of stock.");
        return;
    }

    if (quantity > stock) {
        alert("Only " + stock + " unit(s) available in stock.");
        return;
    }

    const subtotal = unitPrice * quantity;
    const discountAmount = subtotal * (discountPercent / 100);
    const finalTotal = subtotal - discountAmount;

    $('#subtotalOutput').val("Rs. " + subtotal.toFixed(2));
    $('#finalOutput').val("Rs. " + finalTotal.toFixed(2));
}

function addProduct() {
    const productSelect = document.getElementById("productSelect");
    const itemId = productSelect.value;
    const productText = $('#productSelect option:selected').text();
    const quantity = parseInt(document.getElementById("quantityInput").value);
    const unitPrice = parseFloat(document.getElementById("unitPriceInput").value);
    const discountPercent = parseFloat(document.getElementById("discountPercent").value);

    if (!itemId || isNaN(quantity) || isNaN(unitPrice)) {
        alert("Please calculate subtotal first.");
        return;
    }

    if (document.getElementById("row_" + itemId)) {
        alert("This product has already been added.");
        return;
    }

    const subtotal = unitPrice * quantity;
    const discountAmount = subtotal * (discountPercent / 100);
    const finalTotal = subtotal - discountAmount;

    const row = document.createElement("tr");
    row.id = "row_" + itemId;

    row.innerHTML = `
        <td>${productText}<input type="hidden" name="productId[]" value="${itemId}"></td>
        <td>${quantity}<input type="hidden" name="quantity[]" value="${quantity}"></td>
        <td>Rs. ${unitPrice.toFixed(2)}<input type="hidden" name="unitPrice[]" value="${unitPrice.toFixed(2)}"></td>
        <td>Rs. ${subtotal.toFixed(2)}</td>
        <td>${discountPercent.toFixed(2)}%</td>
        <td class="item-total" data-amount="${finalTotal.toFixed(2)}">Rs. ${finalTotal.toFixed(2)}</td>
        <td><button type="button" class="btn btn-outline btn-sm" onclick="removeProductRow('${row.id}')"><i class="fas fa-trash"></i> Remove</button></td>
    `;

    document.getElementById("productTableBody").appendChild(row);

    $('#productSelect').val('').trigger('change');
    $('#quantityInput').val(1);
    $('#unitPriceInput').val('');
    clearTotals();
    recalculateGrandTotal();
}

function removeProductRow(rowId) {
    document.getElementById(rowId).remove();
    recalculateGrandTotal();
}

function clearTotals() {
    $('#subtotalOutput').val('');
    $('#finalOutput').val('');
}

function recalculateGrandTotal() {
    let total = 0;
    document.querySelectorAll('.item-total').forEach(cell => {
        const amount = parseFloat(cell.getAttribute("data-amount"));
        if (!isNaN(amount)) {
            total += amount;
        }
    });
    document.getElementById("grandTotal").innerText = "Rs. " + total.toFixed(2);
}

function validateBeforeSubmit() {
    const productCount = document.querySelectorAll('input[name="productId[]"]').length;
    if (productCount === 0) {
        alert("Please add at least one product before submitting.");
        return false;
    }

    const grandTotalText = document.getElementById("grandTotal").innerText;
    const finalAmount = parseFloat(grandTotalText.replace("Rs. ", ""));
    if (isNaN(finalAmount) || finalAmount <= 0) {
        alert("Final Amount cannot be zero.");
        return false;
    }
    document.getElementById("finalAmountInput").value = finalAmount.toFixed(2);
    return true;
}
