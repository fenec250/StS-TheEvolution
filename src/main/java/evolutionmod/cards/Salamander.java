package evolutionmod.cards;

import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import evolutionmod.orbs.LavafolkGene2;
import evolutionmod.orbs.ShadowGene2;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.powers.SalamanderPower;

import java.util.List;

public class Salamander extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:Salamander";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Salamander.png";
    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;
    private static final int SALAMANDER_AMT = 1;

    public Salamander() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = SALAMANDER_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new SalamanderPower(p, this.magicNumber)));
//        if (this.upgraded) {
//            addToBot(new LizardGene().getChannelAction());
//            addToBot(new LavafolkGene().getChannelAction());
//        } else {
//            formEffect(LizardGene.ID, () -> addToBot(new LavafolkGene().getChannelAction()));
//        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Salamander();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADED_COST);
//            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        if (customTooltips == null) {
            super.getCustomTooltips();
            this.customTooltips.add(LavafolkGene2.TOOLTIP);
            this.customTooltips.add(ShadowGene2.TOOLTIP);
        }
        return customTooltips;
    }

    public static class SalamanderPower extends AbstractPower {
        public static final String POWER_ID = "evolutionmod:SalamanderPower2";
        public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        public static final String NAME = cardStrings.NAME;
        public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

        public static int DEBUFF_DAMAGE = 1;
        public static int EXTRA_DAMAGE = 1;

        public SalamanderPower(AbstractCreature owner, int initialAmount) {
            this.name = NAME;
            this.ID = POWER_ID;
            this.owner = owner;
            this.region128 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/SalamanderPower84.png"), 0, 0, 84, 84);
            this.region48 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/SalamanderPower32.png"), 0, 0, 32, 32);
            this.type = PowerType.BUFF;
            this.amount = initialAmount;
            this.updateDescription();
        }

        @Override
        public void updateDescription() {
            description = DESCRIPTIONS[0] + extraDamage() + DESCRIPTIONS[1]
                    + debuffDamage() + DESCRIPTIONS[2];
        }

        public void stackPower(int stackAmount) {
            this.fontScale = 8.0F;
            this.amount += stackAmount;
            if (this.amount == 0) {
                AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
            }
        }

        private int debuffDamage() {
            return DEBUFF_DAMAGE;
        }

        private int extraDamage() {
            return EXTRA_DAMAGE * this.amount;
        }

        public static int getDebuffDamage() {
            if (CardCrawlGame.isInARun()) {
                if (AbstractDungeon.player.hasPower(POWER_ID)) {
                    return ((SalamanderPower)AbstractDungeon.player.getPower(POWER_ID)).debuffDamage();
                }
            }
            return 0;
        }

        public static int getExtraDamage() {
            if (CardCrawlGame.isInARun()) {
                if (AbstractDungeon.player.hasPower(POWER_ID)) {
                    return ((SalamanderPower)AbstractDungeon.player.getPower(POWER_ID)).extraDamage();
                }
            }
            return 0;
        }
    }
}