function changePage(cnt){
    //let data = await fetch(`/admin/users?page=${cnt}`);
    // Handle the response data
    window.location.href = `/admin/user?page=${cnt}`;
}