"use client";

import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { useEffect, useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { status } from "@/src/utils/statusOrders";

export default function SelectStatus({ onRefresh }: { onRefresh: () => void }) {
  const router = useRouter();
  const searchParams = useSearchParams();
  const selectedCategory = searchParams.get("status") ?? "all";

  function handleSelectChange(value: string) {
    const params = new URLSearchParams(searchParams.toString());

    if (value === "all") {
      params.delete("status");
    } else {
      params.set("status", value);
    }

    router.push(`?${params.toString()}`);
  }

  useEffect(() => {
    onRefresh();
  }, [searchParams]);

  return (
    <>
    <div className="d-flex items-start">
      <label className="text-sm mb-2">
          Filtrar por:
      </label>
      <div className="w-42">
        <Select onValueChange={handleSelectChange} value={selectedCategory}>
          <SelectTrigger className="w-full">
            <SelectValue placeholder="Selecione o status" />
          </SelectTrigger>

          <SelectContent className="w-full">
            <SelectItem value="all">Selecione o status</SelectItem>

            {status.map(item => (
              <SelectItem key={item.id} value={item.value}>
                {item.label}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
      </div>

    </div>
    </>
  );
}
