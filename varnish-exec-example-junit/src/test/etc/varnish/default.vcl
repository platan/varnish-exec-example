backend localhost {
    .host = "127.0.0.1";
    .port = "8080";
}

sub vcl_fetch {
    if (beresp.http.Surrogate-Control ~ "ESI/1.0") {
        unset beresp.http.Surrogate-Control;
        set beresp.do_esi = true;
    }
}

sub vcl_recv {
    set req.backend = localhost;

    return(pass);
}
