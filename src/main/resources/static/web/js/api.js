$(function () {
    apiProvince=(prodvince)=>{
        let district;
    
        prodvince.forEach(element => {
            $('#province').append(`<option value="${element.code}">${element.name}</option>`)
        });
        $('#province').change(function () {
            $('#district').html('<option value="-1">Chọn quận/huyện</option>')
            $('#town').html('<option value = "-1"> Chọn phường/xã </option>')
            let value = $(this).val();
            $.each(prodvince,function(index,element){
                if (element.code == value) {
                    district = element.districts;
                    $.each(element.districts,function(index,element1){
                        $('#district').append(`<option value="${element1.code}">${element1.name}</option>`)
                    })
                    
                }
            })         
        });    
        $('#district').change(function () {
            $('#town').html('<option value = "-1"> Chọn phường/xã </option>')
            let value = $(this).val();
            $.each(district,function(index,element){
                if (element.code == value) {
                    element.wards.forEach(element1 => {
                        $('#town').append(`<option value="${element1.code}">${element1.name}</option>`)
                    });
                }
            })       
        });
    }
    prodvince = JSON.parse(data);
     apiProvince(prodvince);
})

// của giỏ hàng
document.getElementById("cart-icon").addEventListener("mouseenter", function() {
    // Gửi yêu cầu lấy sản phẩm trong giỏ hàng
    fetch('/get-mini-cart')  // API này trả về danh sách sản phẩm trong giỏ hàng
        .then(response => response.json())
        .then(data => {
            const miniCartProducts = document.getElementById("miniCartProducts");
            miniCartProducts.innerHTML = ''; // Xóa nội dung cũ

            // Kiểm tra nếu có sản phẩm trong giỏ hàng
            if (data && data.length > 0) {
                data.forEach(product => {
                    const productElement = document.createElement("div");
                    productElement.classList.add("mini-cart-product");
                    productElement.innerHTML = `
                        <img src="${product.image}" alt="${product.name}" width="50" height="50">
                        <span>${product.name}</span>
                        <span>x${product.amount}</span>
                    `;
                    miniCartProducts.appendChild(productElement);
                });
            } else {
                miniCartProducts.innerHTML = "<p>Giỏ hàng trống</p>";
            }
        })
        .catch(error => console.error('Lỗi khi lấy giỏ hàng:', error));
});