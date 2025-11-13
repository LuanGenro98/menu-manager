import LoginForm from "../api/login/_components/login";

export default async function Login(){

    let data = {
        "email": "",
        "password": ""
    };

    return (
        <LoginForm login={data}/>
    )
}