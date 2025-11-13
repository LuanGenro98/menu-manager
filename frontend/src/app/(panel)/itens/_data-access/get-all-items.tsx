import { apiGet } from "@/lib/api";

export default async function getAllItems(){

    const result = await apiGet("auth/items");    

    if(result.error){
        return [];
    }

    return result;
}