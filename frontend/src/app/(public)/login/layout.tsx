import { ModeToggle } from "../_components/mode-toggle";

export default function LoginLayout({ children } : { children: React.ReactNode }){
  return (
    <>
      {children}
      <ModeToggle />
    </>
  )

}