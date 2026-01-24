package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.LymeanGene2;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.powers.FatePower;

public class Visions
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:Visions";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Visions.png";
    private static final int COST = 0;
    private static final int FATE_AMT = 1;
    private static final int UPGRADE_FATE_AMT = 2;
    private static final int FORM_FATE_AMT = 2;

    public Visions() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.COMMON, CardTarget.NONE);
        this.magicNumber = this.baseMagicNumber = FATE_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean form = isPlayerInThisForm(LymeanGene2.ID);
        int fate = this.magicNumber + (form ? FORM_FATE_AMT : 0);
        addToBot(new ApplyPowerAction(p, p, new FatePower(p, fate), fate, true));
        formEffect(LymeanGene2.ID);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Visions();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_FATE_AMT);
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(LymeanGene2.ID)) {
            this.glowColor = LymeanGene2.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}