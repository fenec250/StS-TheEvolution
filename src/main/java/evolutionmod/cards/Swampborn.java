package evolutionmod.cards;

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
import evolutionmod.orbs.LizardGene2;
import evolutionmod.patches.EvolutionEnum;

public class Swampborn extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:Swampborn";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Swampborn.png";
    private static final int COST = 2;
    private static final int POWER_AMT = 1;
    private static final int UPGRADED_COST = 1;

    public Swampborn() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.RARE, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = POWER_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new SwampbornPower(p, this.magicNumber)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Swampborn();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADED_COST);
        }
    }


    public static class SwampbornPower extends AbstractPower {
        public static final String POWER_ID = "evolutionmod:SwampbornPower";
        public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        public static final String NAME = cardStrings.NAME;
        public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

        public SwampbornPower(AbstractCreature owner, int initialAmount) {
            this.name = NAME;
            this.ID = POWER_ID;
            this.owner = owner;
            this.region128 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/AphrodisiacPower84.png"), 0, 0, 84, 84);
            this.region48 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/AphrodisiacPower32.png"), 0, 0, 32, 32);
            this.type = PowerType.BUFF;
            this.amount = initialAmount;
            this.updateDescription();
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

        public void atStartOfTurn() {
            for (int i = 0; i < this.amount;++i) {
                addToBot(new LizardGene2().getChannelAction());
            }
        }
    }
}