$(document).ready(function () {
    $('.select2').select2();
    setDiscount();

    // Handle URL Status Alerts (success, failed, invalid)
    const urlParams = new URLSearchParams(window.location.search);
    const status = urlParams.get("status");

    if (status === "success") {
        Swal.fire("Success!", "Bill created successfully!", "success");
    } else if (status === "failed") {
        Swal.fire("Error", "Failed to create the bill. Please try again.", "error");
    } else if (status === "invalid") {
        Swal.fire("Warning", "Please provide valid bill details.", "warning");
    }

    // Product Select Change Handler
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
        Swal.fire("Warning", "Please select a product.", "warning");
        return;
    }

    if (isNaN(quantity) || quantity <= 0 || isNaN(unitPrice)) {
        Swal.fire("Warning", "Please enter a valid quantity.", "warning");
        return;
    }

    if (stock === 0) {
        Swal.fire("Out of Stock", "This product is sold out.", "error");
        return;
    }

    if (quantity > stock) {
        Swal.fire("Stock Limit", "Only " + stock + " unit(s) available in stock.", "warning");
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
        Swal.fire("Warning", "Please calculate subtotal first.", "warning");
        return;
    }

    if (document.getElementById("row_" + itemId)) {
        Swal.fire("Duplicate", "This product has already been added.", "info");
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

    // Reset form fields
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
        Swal.fire("Warning", "Please add at least one product before submitting.", "warning");
        return false;
    }

    const grandTotalText = document.getElementById("grandTotal").innerText;
    const finalAmount = parseFloat(grandTotalText.replace("Rs. ", ""));
    if (isNaN(finalAmount) || finalAmount <= 0) {
        Swal.fire("Warning", "Final Amount cannot be zero.", "warning");
        return false;
    }
    document.getElementById("finalAmountInput").value = finalAmount.toFixed(2);
    return true;
}

