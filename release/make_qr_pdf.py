import sys
import qrcode
from PIL import Image, ImageDraw, ImageFont

out = sys.argv[1]
W, H = 2480, 3508
FONT = "C:/Windows/Fonts/arial.ttf"
page = Image.new("RGB", (W, H), "white")
draw = ImageDraw.Draw(page)

def centered(text, y, size):
    font = ImageFont.truetype(FONT, size)
    width = draw.textlength(text, font=font)
    draw.text(((W - width) / 2, y), text, fill="black", font=font)

centered("Claramente AR - QR codes", 140, 50)

items = [
    ("Sistema Solar", "claramente:solar"),
    ("Molécula de água", "claramente:agua"),
    ("Sólidos geométricos", "claramente:solidos"),
]
size = 584
for index, (title, data) in enumerate(items):
    top = 446 + index * 1029
    qr = qrcode.QRCode(error_correction=qrcode.constants.ERROR_CORRECT_M, border=0, box_size=1)
    qr.add_data(data)
    qr.make(fit=True)
    image = qr.make_image(fill_color="black", back_color="white").convert("RGB").resize((size, size), Image.NEAREST)
    page.paste(image, ((W - size) // 2, top))
    centered(title, top + size + 110, 42)
    centered(data, top + size + 205, 30)

centered("No app: toque no modelo, escolha \"QR code\" e depois \"Ler QR code em AR\".", 3395, 28)
centered("Imprima em tamanho normal; o QR decide o conteúdo.", 3440, 28)
page.save(out, "PDF", resolution=300.0)
print("saved", out)
