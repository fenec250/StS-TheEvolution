package evolutionmod.cards;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.relics.ChemicalX;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.powers.GrowthPower;

import java.util.List;

public class Barkskin
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:Barkskin";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/BarkskinP.png";
    private static final int COST = -1;
    private static final int ARMOR_AMT = 2;
    private static final int UPGRADE_EFFECT_AMT = 1;

    public Barkskin() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.RARE, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = ARMOR_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int x = energyOnUse;
        if (p.hasRelic(ChemicalX.ID)) {
            x += 2;
            p.getRelic(ChemicalX.ID).flash();
        }
        if (upgraded) {
            x += UPGRADE_EFFECT_AMT;
        }

        if (x > 0) {
            addToBot(new ApplyPowerAction(p, p, new PlatedArmorPower(p, x * this.magicNumber), x * this.magicNumber));
        }
        addToBot(new ApplyPowerAction(p, p, new BarkskinPower(p, 1), 1));
        if (x > 0) {
            addToBot(new ApplyPowerAction(p, p, new GrowthPower(p, x), x));
        }
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (!freeToPlayOnce) {
                    p.energy.use(energyOnUse);
                }
                this.isDone = true;
            }
        });
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        if (customTooltips == null) {
            customTooltips = super.getCustomTooltips();
            customTooltips.add(new TooltipInfo("Plated Armor", "At the end of your turn, gain this amount of #yBlock. NL Receiving unblocked attack damage reduces #yPlated #yArmor by #b1."));
        }
        return customTooltips;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
    public static class BarkskinPower extends AbstractPower {
        public static final String POWER_ID = "evolutionmod:BarkskinPower";
        public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        public static final String NAME = cardStrings.NAME;
        public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

        public BarkskinPower(AbstractCreature owner, int initialAmount) {
            this.name = NAME;
            this.ID = POWER_ID;
            this.owner = owner;
//            this.region128 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/SymbiotesPower84.png"), 0, 0, 84, 84);
//            this.region48 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/SymbiotesPower32.png"), 0, 0, 32, 32);
            this.loadRegion("barricade");
            this.type = PowerType.BUFF;
            this.amount = initialAmount;
            this.updateDescription();
        }

        @Override
        public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
            super.onApplyPower(power, target, source);
            if (power.ID.equals(GrowthPower.POWER_ID)) {
                int total = power.amount;
                if (target.hasPower(GrowthPower.POWER_ID)) {
                    total += target.getPower(GrowthPower.POWER_ID).amount;
                }
                int armor = total/GrowthPower.ENERGY_THRESHOLD;
                if (armor > 0) {
                    armor *= this.amount;
                    this.flash();
                    addToBot(new ApplyPowerAction(this.owner, this.owner, new PlatedArmorPower(this.owner, armor), armor));
                }
            }
        }

        @Override
        public void updateDescription() {
            description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        }

        public void stackPower(int stackAmount) {
            this.fontScale = 8.0F;
            this.amount += stackAmount;
            if (this.amount <= 0) {
                AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
            }
        }
    }
}