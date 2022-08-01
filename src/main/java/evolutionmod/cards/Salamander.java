package evolutionmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.orbs.LizardGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.SalamanderPower;

public class Salamander extends BaseEvoCard implements GlowingCard {
    public static final String ID = "evolutionmod:Salamander";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Salamander.png";
    private static final int COST = 1;
    private static final int SALAMANDER_AMT = 1;

    public Salamander() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = SALAMANDER_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new SalamanderPower(p, this.magicNumber)));
        if (this.upgraded) {
            addToBot(new LizardGene().getChannelAction());
            addToBot(new LavafolkGene().getChannelAction());
        } else {
            formEffect(LizardGene.ID, () -> addToBot(new LavafolkGene().getChannelAction()));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Salamander();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public int getNumberOfGlows() {
        return upgraded ? 0 : 1;
    }

    @Override
    public boolean isGlowing(int glowIndex) {
        return isPlayerInThisForm(LizardGene.ID);
    }

    @Override
    public Color getGlowColor(int glowIndex) {
        return LizardGene.COLOR.cpy();
    }
}